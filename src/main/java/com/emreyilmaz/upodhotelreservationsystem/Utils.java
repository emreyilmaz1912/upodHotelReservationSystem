package com.emreyilmaz.upodhotelreservationsystem;



import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    public static  final Path root = Paths.get(".").normalize().toAbsolutePath();;
    public static  final Path LOGO = Paths.get(root.toString(),"src", "main", "resources", "com", "emreyilmaz", "upodhotelreservationsystem","img","icon_upod.png");

}
