package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;

public class Win_Lab extends Frame implements ActionListener {
    Button bex = new Button("Выход");
    Button sea = new Button("Поиск");
    TextArea txa = new TextArea();

    public Win_Lab() {
        super("Лаба 1");
        setLayout(null);
        setSize(450, 250);
        setBackground(new Color(200, 100, 150));

        // Установка шрифта для кнопок
        Font font = new Font("Arial", Font.BOLD, 12);
        bex.setFont(font);
        sea.setFont(font);

        // Изменение цвета кнопок
        bex.setBackground(new Color(70, 130, 180));  // Цвет кнопки "Exit"
        sea.setBackground(new Color(70, 130, 180));  // Цвет кнопки "Search"
        bex.setForeground(Color.WHITE);
        sea.setForeground(Color.WHITE);

        // Изменяем текстовое поле
        txa.setFont(new Font("Arial", Font.PLAIN, 12));
        txa.setBackground(new Color(240, 240, 240)); // Светлый фон для текстового поля
        txa.setForeground(Color.BLACK); // Темный цвет текста

        // Задаем размеры и позиции компонентов
        txa.setBounds(20, 50, 400, 100);
        bex.setBounds(300, 190, 100, 30);
        sea.setBounds(50, 190, 100, 30);

        // Добавляем компоненты в окно
        add(txa);
        add(bex);
        add(sea);

        // Добавляем обработчики событий
        bex.addActionListener(this);
        sea.addActionListener(this);

        // Устанавливаем видимость окна
        setVisible(true);
        setLocationRelativeTo(null);

        // Закрытие приложения при закрытии окна
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bex)
            System.exit(0);
        else if (ae.getSource() == sea) {
            // Формируем массив ключевых слов
            String[] keywords = txa.getText().split(",");
            for (int j = 0; j < keywords.length; j++) {
                System.out.println(keywords[j]);
            }

            // Задаем директорию с файлами
            File f = new File("D:/photo_laba");
            ArrayList<File> files = new ArrayList<>(Arrays.asList(f.listFiles()));

            txa.setText("");

            // Переменные для хранения информации о файле с наибольшим числом совпадений
            File maxFile = null;
            int maxCoincidence = -1;

            // Цикл для проверки каждого файла
            for (File elem : files) {
                int zcoincidence = test_url(elem, keywords);
                txa.append("\n" + elem + "  :" + zcoincidence);

                // Обновляем файл с наибольшим числом совпадений
                if (zcoincidence > maxCoincidence) {
                    maxCoincidence = zcoincidence;
                    maxFile = elem;
                }
            }

            // Открываем файл с наибольшим числом совпадений в браузере
            if (maxFile != null && maxCoincidence > 0) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(maxFile.toURI());
                } catch (IOException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
            }
        }
    }

    // Метод для проверки совпадений ключевых слов в файле
    public static int test_url(File elem, String[] keywords) {
        int res = 0;
        URL url = null;
        URLConnection con = null;
        int i;

        try {
            String ffele = "" + elem;
            url = new URL("file:/" + ffele.trim());
            con = url.openConnection();
            File file = new File("D:/rezult/test.html");
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            String bhtml = ""; // file content in byte array

            while ((i = bis.read()) != -1) {
                bos.write(i);
                bhtml += (char) i;
            }

            bos.flush();
            bis.close();
            String htmlcontent = (new String(bhtml)).toLowerCase(); // file content in string
            System.out.println("New url content is: " + htmlcontent);

            // Проверка наличия ключевых слов в содержимом файла
            for (int j = 0; j < keywords.length; j++) {
                if (htmlcontent.indexOf(keywords[j].trim().toLowerCase()) >= 0)
                    res++;
            }
        } catch (MalformedInputException malformedInputException) {
            System.out.println("Error: " + malformedInputException.getMessage());
            return -1;
        } catch (IOException ioException) {
            System.out.println("Error: " + ioException.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
        return res;
    }
}
