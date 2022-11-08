package mk.ukim.finki.av5.grades;

import java.io.*;

public class FacultyTest {

    public static void main(String[] args) {
        Course course = new Course();

        File inputFile = new File("C:\\Users\\koki_\\Desktop\\npProj\\no_2022\\src\\mk\\ukim\\finki\\av5\\files\\students");
        File outPutFile = new File("C:\\Users\\koki_\\Desktop\\npProj\\no_2022\\src\\mk\\ukim\\finki\\av5\\files\\result");

        try {
            course.readData(new FileInputStream(inputFile));

            System.out.println("-------- Printing sorted ---------");
            course.printSortedData(System.out);

            System.out.println("-------- Printing details ---------");
            course.printDetailedData(new FileOutputStream(outPutFile));

            System.out.println("-------- Printing distribution ---------");
            course.printGradeDistribution(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
