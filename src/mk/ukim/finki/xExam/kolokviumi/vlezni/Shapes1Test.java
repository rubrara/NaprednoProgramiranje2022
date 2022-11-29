package mk.ukim.finki.xExam.kolokviumi.vlezni;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    private int side;

    public Square(int side) {
        this.side = side;
    }

    public int getPerimeter() {
        return side * 4;
    }
}

class Canvas implements Comparable<Canvas> {
    private String id;
    private List<Square> squareList;

    public Canvas(String id, List<Square> squareList) {
        this.id = id;
        this.squareList = squareList;
    }

    public static Canvas stringToCanvas(String line) {
        String[] parts = line.split("\\s+");

        String idTmp = parts[0];
        List<Square> tmpSquare = new ArrayList<>();

        Arrays.stream(parts).skip(1).forEach(part -> tmpSquare.add(new Square(Integer.parseInt(part))));

        return new Canvas(idTmp, tmpSquare);
    }

    public String getId() {
        return id;
    }

    public List<Square> getSquareList() {
        return squareList;
    }

    public int totalPerimeter() {
        return this.squareList.stream().mapToInt(Square::getPerimeter).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", this.id, this.squareList.size(), this.totalPerimeter());
    }

    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(totalPerimeter(), o.totalPerimeter());
    }
}

class ShapesApplication {

    private List<Canvas> canvasList = new ArrayList<>();

    public int readCanvases(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        canvasList = reader.lines().map(Canvas::stringToCanvas).collect(Collectors.toList());

        return canvasList.stream().mapToInt(canvas -> canvas.getSquareList().size()).sum();
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        Canvas canvas = canvasList.stream().max(Comparator.comparing(Canvas::totalPerimeter)).get();

        writer.println(canvas);

        writer.println();
        writer.println(canvasList.stream().min(Comparator.naturalOrder()).get());


        writer.flush();
    }

}

public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        File file = new File("C:\\Users\\koki_\\Desktop\\npProj\\no_2022\\src\\mk\\ukim\\finki\\xExam\\kolokviumi\\vlezni\\files\\canvases");

        System.out.println("===READING SHAPES FROM INPUT STREAM===");
        try {
            System.out.println(shapesApplication.readCanvases(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);
    }
}
