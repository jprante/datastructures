package com.dslplatform.json;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public abstract class JavaGeomConverter {

    static final JsonReader.ReadObject<Point2D.Double> LocationReader = new JsonReader.ReadObject<Point2D.Double>() {
        @Nullable
        @Override
        public Point2D.Double read(JsonReader reader) throws IOException {
            return reader.wasNull() ? null : deserializeLocation(reader);
        }
    };
    static final JsonWriter.WriteObject<Point2D> LocationWriter = new JsonWriter.WriteObject<Point2D>() {
        @Override
        public void write(JsonWriter writer, @Nullable Point2D value) {
            serializeLocationNullable(value, writer);
        }
    };
    static final JsonReader.ReadObject<Point> PointReader = new JsonReader.ReadObject<Point>() {
        @Nullable
        @Override
        public Point read(JsonReader reader) throws IOException {
            return reader.wasNull() ? null : deserializePoint(reader);
        }
    };
    static final JsonWriter.WriteObject<Point> PointWriter = new JsonWriter.WriteObject<Point>() {
        @Override
        public void write(JsonWriter writer, @Nullable Point value) {
            serializePointNullable(value, writer);
        }
    };
    static final JsonReader.ReadObject<Rectangle2D.Double> RectangleReader = new JsonReader.ReadObject<Rectangle2D.Double>() {
        @Nullable
        @Override
        public Rectangle2D.Double read(JsonReader reader) throws IOException {
            return reader.wasNull() ? null : deserializeRectangle(reader);
        }
    };
    static final JsonWriter.WriteObject<Rectangle2D> RectangleWriter = new JsonWriter.WriteObject<Rectangle2D>() {
        @Override
        public void write(JsonWriter writer, @Nullable Rectangle2D value) {
            serializeRectangleNullable(value, writer);
        }
    };
    static final JsonWriter.WriteObject<Image> ImageWriter = new JsonWriter.WriteObject<Image>() {
        @Override
        public void write(JsonWriter writer, @Nullable Image value) {
            serialize(value, writer);
        }
    };
    static final JsonReader.ReadObject<BufferedImage> ImageReader = new JsonReader.ReadObject<BufferedImage>() {
        @Nullable
        @Override
        public BufferedImage read(JsonReader reader) throws IOException {
            return reader.wasNull() ? null : deserializeImage(reader);
        }
    };

    public static void serializeLocationNullable(@Nullable final Point2D value, final JsonWriter sw) {
        if (value == null) {
            sw.writeNull();
        } else {
            serializeLocation(value, sw);
        }
    }

    public static void serializeLocation(final Point2D value, final JsonWriter sw) {
        sw.writeAscii("{\"X\":");
        NumberConverter.serialize(value.getX(), sw);
        sw.writeAscii(",\"Y\":");
        NumberConverter.serialize(value.getY(), sw);
        sw.writeByte(JsonWriter.OBJECT_END);
    }

    public static Point2D.Double deserializeLocation(final JsonReader reader) throws IOException {
        if (reader.last() != '{') {
            //TODO: support compact version [X,Y] without names
            throw reader.newParseError("Expecting '{' for object start");
        }
        byte nextToken = reader.getNextToken();
        if (nextToken == '}') {
            return new Point2D.Double();
        }
        double x = 0;
        double y = 0;
        String name = StringConverter.deserialize(reader);
        nextToken = reader.getNextToken();
        if (nextToken != ':') {
            throw reader.newParseError("Expecting ':' after attribute name");
        }
        reader.getNextToken();
        double value = NumberConverter.deserializeDouble(reader);
        if ("X".equalsIgnoreCase(name)) {
            x = value;
        } else if ("Y".equalsIgnoreCase(name)) {
            y = value;
        }
        while ((nextToken = reader.getNextToken()) == ',') {
            reader.getNextToken();
            name = StringConverter.deserialize(reader);
            nextToken = reader.getNextToken();
            if (nextToken != ':') {
                throw reader.newParseError("Expecting ':' after attribute name");
            }
            reader.getNextToken();
            value = NumberConverter.deserializeDouble(reader);
            if ("X".equalsIgnoreCase(name)) {
                x = value;
            } else if ("Y".equalsIgnoreCase(name)) {
                y = value;
            }
        }
        if (nextToken != '}') {
            throw reader.newParseError("Expecting '}' for object end");
        }
        return new Point2D.Double(x, y);
    }

    public static ArrayList<Point2D> deserializeLocationCollection(final JsonReader reader) throws IOException {
        final ArrayList<Point2D> res = new ArrayList<Point2D>(4);
        reader.deserializeCollection(LocationReader, res);
        return res;
    }

    public static void deserializeLocationCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
        reader.deserializeCollection(LocationReader, res);
    }

    public static ArrayList<Point2D> deserializeLocationNullableCollection(final JsonReader reader) throws IOException {
        final ArrayList<Point2D> res = new ArrayList<Point2D>(4);
        reader.deserializeNullableCollection(LocationReader, res);
        return res;
    }

    public static void deserializeLocationNullableCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
        reader.deserializeNullableCollection(LocationReader, res);
    }

    public static void serializePointNullable(@Nullable final Point value, final JsonWriter sw) {
        if (value == null) {
            sw.writeNull();
        } else {
            serializePoint(value, sw);
        }
    }

    public static void serializePoint(final Point value, final JsonWriter sw) {
        sw.writeAscii("{\"X\":");
        NumberConverter.serialize(value.x, sw);
        sw.writeAscii(",\"Y\":");
        NumberConverter.serialize(value.y, sw);
        sw.writeByte(JsonWriter.OBJECT_END);
    }

    public static Point deserializePoint(final JsonReader reader) throws IOException {
        if (reader.last() != '{') {
            throw reader.newParseError("Expecting '{' for object start");
        }
        byte nextToken = reader.getNextToken();
        if (nextToken == '}') {
            return new Point();
        }
        int x = 0;
        int y = 0;
        String name = StringConverter.deserialize(reader);
        nextToken = reader.getNextToken();
        if (nextToken != ':') {
            throw reader.newParseError("Expecting ':' after attribute name");
        }
        reader.getNextToken();
        int value = NumberConverter.deserializeInt(reader);
        if ("X".equalsIgnoreCase(name)) {
            x = value;
        } else if ("Y".equalsIgnoreCase(name)) {
            y = value;
        }
        while ((nextToken = reader.getNextToken()) == ',') {
            reader.getNextToken();
            name = StringConverter.deserialize(reader);
            nextToken = reader.getNextToken();
            if (nextToken != ':') {
                throw reader.newParseError("Expecting ':' after attribute name");
            }
            reader.getNextToken();
            value = NumberConverter.deserializeInt(reader);
            if ("X".equalsIgnoreCase(name)) {
                x = value;
            } else if ("Y".equalsIgnoreCase(name)) {
                y = value;
            }
        }
        if (nextToken != '}') {
            throw reader.newParseError("Expecting '}' for object end");
        }
        return new Point(x, y);

    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Point> deserializePointCollection(final JsonReader reader) throws IOException {
        return reader.deserializeCollection(PointReader);
    }

    public static void deserializePointCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
        reader.deserializeCollection(PointReader, res);
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Point> deserializePointNullableCollection(final JsonReader reader) throws IOException {
        return reader.deserializeNullableCollection(PointReader);
    }

    public static void deserializePointNullableCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
        reader.deserializeNullableCollection(PointReader, res);
    }

    public static void serializeRectangleNullable(@Nullable final Rectangle2D value, final JsonWriter sw) {
        if (value == null) {
            sw.writeNull();
        } else {
            serializeRectangle(value, sw);
        }
    }

    public static void serializeRectangle(final Rectangle2D value, final JsonWriter sw) {
        sw.writeAscii("{\"X\":");
        NumberConverter.serialize(value.getX(), sw);
        sw.writeAscii(",\"Y\":");
        NumberConverter.serialize(value.getY(), sw);
        sw.writeAscii(",\"Width\":");
        NumberConverter.serialize(value.getWidth(), sw);
        sw.writeAscii(",\"Height\":");
        NumberConverter.serialize(value.getHeight(), sw);
        sw.writeByte(JsonWriter.OBJECT_END);
    }

    public static Rectangle2D.Double deserializeRectangle(final JsonReader reader) throws IOException {
        if (reader.last() != '{') {
            throw reader.newParseError("Expecting '{' for object start");
        }
        byte nextToken = reader.getNextToken();
        if (nextToken == '}') {
            return new Rectangle2D.Double();
        }
        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;
        String name = StringConverter.deserialize(reader);
        nextToken = reader.getNextToken();
        if (nextToken != ':') {
            throw reader.newParseError("Expecting ':' after attribute name");
        }
        reader.getNextToken();
        double value = NumberConverter.deserializeDouble(reader);
        if ("X".equalsIgnoreCase(name)) {
            x = value;
        } else if ("Y".equalsIgnoreCase(name)) {
            y = value;
        } else if ("Width".equalsIgnoreCase(name)) {
            width = value;
        } else if ("Height".equalsIgnoreCase(name)) {
            height = value;
        }
        while ((nextToken = reader.getNextToken()) == ',') {
            reader.getNextToken();
            name = StringConverter.deserialize(reader);
            nextToken = reader.getNextToken();
            if (nextToken != ':') {
                throw reader.newParseError("Expecting ':' after attribute name");
            }
            reader.getNextToken();
            value = NumberConverter.deserializeDouble(reader);
            if ("X".equalsIgnoreCase(name)) {
                x = value;
            } else if ("Y".equalsIgnoreCase(name)) {
                y = value;
            } else if ("Width".equalsIgnoreCase(name)) {
                width = value;
            } else if ("Height".equalsIgnoreCase(name)) {
                height = value;
            }
        }
        if (nextToken != '}') {
            throw reader.newParseError("Expecting '}' for object end");
        }
        return new Rectangle2D.Double(x, y, width, height);
    }

    public static ArrayList<Rectangle2D> deserializeRectangleCollection(final JsonReader reader) throws IOException {
        final ArrayList<Rectangle2D> res = new ArrayList<Rectangle2D>(4);
        reader.deserializeCollection(RectangleReader, res);
        return res;
    }

    public static void deserializeRectangleCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
        reader.deserializeCollection(RectangleReader, res);
    }

    public static ArrayList<Rectangle2D> deserializeRectangleNullableCollection(final JsonReader reader) throws IOException {
        final ArrayList<Rectangle2D> res = new ArrayList<Rectangle2D>(4);
        reader.deserializeNullableCollection(RectangleReader, res);
        return res;
    }

    public static void deserializeRectangleNullableCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
        reader.deserializeNullableCollection(RectangleReader, res);
    }

    public static void serialize(@Nullable final Image value, final JsonWriter sw) {
        if (value == null) {
            sw.writeNull();
            return;
        }
        final RenderedImage image;
        if (value instanceof RenderedImage) {
            image = (RenderedImage) value;
        } else {
            final BufferedImage bufferedImage = new BufferedImage(value.getWidth(null), value.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics bGr = bufferedImage.createGraphics();
            bGr.drawImage(value, 0, 0, null);
            bGr.dispose();
            image = bufferedImage;
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(image, "png", baos);
            BinaryConverter.serialize(baos.toByteArray(), sw);
        } catch (final IOException e) {
            throw new SerializationException(e);
        }
    }

    public static BufferedImage deserializeImage(final JsonReader reader) throws IOException {
        final byte[] content = BinaryConverter.deserialize(reader);
        return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(content));
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BufferedImage> deserializeImageCollection(final JsonReader reader) throws IOException {
        return reader.deserializeCollection(ImageReader);
    }

    public static void deserializeImageCollection(final JsonReader reader, final Collection<BufferedImage> res) throws IOException {
        reader.deserializeCollection(ImageReader, res);
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BufferedImage> deserializeImageNullableCollection(final JsonReader reader) throws IOException {
        return reader.deserializeNullableCollection(ImageReader);
    }

    public static void deserializeImageNullableCollection(final JsonReader reader, final Collection<BufferedImage> res) throws IOException {
        reader.deserializeNullableCollection(ImageReader, res);
    }
}
