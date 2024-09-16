package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.charset.StandardCharsets; // добавьте этот импорт

public class Win_Lab extends Frame implements ActionListener {
    Button bex = new Button("Exit");
    Button sea = new Button("Search");
    TextArea txa = new TextArea();

    public Win_Lab() {
        super("My Window");
        setLayout(null);
        setBackground(new Color(150, 200, 100));
        setSize(450, 250);
        add(bex);
        add(sea);
        add(txa);
        bex.setBounds(110, 190, 100, 20);
        bex.addActionListener(this);
        sea.setBounds(110, 165, 100, 20);
        sea.addActionListener(this);
        txa.setBounds(20, 50, 300, 100);
        setVisible(true); // Изменено с show на setVisible
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == bex)
            System.exit(0);
        else if (ae.getSource() == sea) {
            String[] keywords = txa.getText().split(",");
            for (int j = 0; j < keywords.length; j++) {
                System.out.println(keywords[j]);

            }
            File f = new File("F:/photo_laba");
            ArrayList<File> files =
                    new ArrayList<File>(Arrays.asList(f.listFiles()));
            txa.setText("");
            for (File elem : files) {
                int zcoincidence = test_url(elem, keywords);
                txa.append("\n" + elem + "  :" + zcoincidence);
            }
        }

    }

    public static int test_url(File elem, String[] keywords) {
        int res = 0;
        URL url = null;
        URLConnection con = null;
        int i;
        try {
            String ffele = "" + elem;
            url = new URL("file:/" + ffele.trim());
            con = url.openConnection();
            File file = new File("F:/rezult/output.html");
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            StringBuilder bhtml = new StringBuilder(); // Используйте StringBuilder вместо String

            while ((i = bis.read()) != -1) {
                bos.write(i);
                bhtml.append((char) i); // Используйте StringBuilder для улучшения производительности
            }
            bos.flush();
            bis.close();

            // Преобразуйте содержимое в строку, указывая кодировку
            String htmlcontent = bhtml.toString().toLowerCase(); // Используйте StringBuilder
            System.out.println("New url content is: " + htmlcontent);

            for (String keyword : keywords) {
                if (htmlcontent.contains(keyword.trim().toLowerCase())) { // Используйте contains вместо indexOf
                    res++;
                }
            }
        } catch (MalformedInputException malformedInputException) {
            System.out.println("error " + malformedInputException.getMessage());
            return -1;
        } catch (IOException ioException) {
            System.out.println("error " + ioException.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            return -1;
        }
        return res;
    }

}
