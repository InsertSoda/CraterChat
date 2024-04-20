package com.insertsoda.craterchat.api.v1.arguments.types;

import com.insertsoda.craterchat.api.v1.arguments.RelativeFloat;
import com.insertsoda.craterchat.impl.arguments.RelativeFloatImpl;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class RelativeFloatType implements ArgumentType<RelativeFloat> {
    private final float minimum;
    private final float maximum;

    private Supplier<Float> deviatedValue;

    private RelativeFloatType(float minimum, float maximum, Supplier<Float> deviatedValue) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.deviatedValue = deviatedValue;
    }

    public static RelativeFloatType argument(){
        return argument(-Float.MAX_VALUE);
    }

    public static RelativeFloatType argument(float minimum){
        return argument(minimum, Float.MAX_VALUE, null);
    }

    public static RelativeFloatType argument(Supplier<Float> deviatedValue){
        return argument(-Float.MAX_VALUE, Float.MAX_VALUE, deviatedValue);
    }

    public static RelativeFloatType argument(float minimum, float maximum, Supplier<Float> deviatedValue){
        return new RelativeFloatType(minimum, maximum, deviatedValue);
    }

    public static RelativeFloat getRelativeFloat(final CommandContext<?> context, final String name) {
        return context.getArgument(name, RelativeFloat.class);
    }

    public float getMinimum() {
        return this.minimum;
    }

    public float getMaximum() {
        return this.maximum;
    }

    @Override
    public RelativeFloat parse(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        float result;
        boolean isRelative = false;

        while(reader.canRead() && reader.peek() != ' '){
            reader.skip();
        }

        String string = reader.getString().substring(start, reader.getCursor());

        try {
            if (string.startsWith("~")) {
                if(string.length() == 1){
                    result = 0;
                } else {
                    result = Float.parseFloat(string.substring(1));
                }
                isRelative = true;
            } else {
                result = Float.parseFloat(string);
            }
        } catch (NumberFormatException e){
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().create(string);
        }

        if(this.deviatedValue != null && isRelative){
            isRelative = false;
            result = this.deviatedValue.get() + result;
        }

        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooHigh().createWithContext(reader, result, maximum);
        }

        return new RelativeFloatImpl(result, isRelative);
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("1","1.5","-3","-59","~1", "~0.5", "~-5", "~-6.7124");
    }
}
