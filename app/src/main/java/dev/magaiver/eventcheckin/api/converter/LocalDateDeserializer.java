package dev.magaiver.eventcheckin.api.converter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocalDateDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return LocalDateTime.parse(parser.readValueAs(String.class));
    }
}