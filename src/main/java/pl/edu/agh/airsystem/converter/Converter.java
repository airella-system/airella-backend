package pl.edu.agh.airsystem.converter;

public interface Converter<F, T> {
    T convert(F f);
}
