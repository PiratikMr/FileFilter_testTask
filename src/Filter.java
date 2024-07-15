import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Filter {
    private String pathToFiles = "";
    private String prefix = "";
    private final Info info = new Info();
    private boolean isAddData = false;

    private String outInfo = "";

    private final HashMap<FileType, String> outText = new HashMap<>();

    private enum InfoType {
        SHORT, FULL, NONE;
    }
    private enum FileType {
        STRINGS, INTEGERS, FLOATS;

        @Override
        public String toString() {
            return switch (this) {
                case STRINGS -> "strings.txt";
                case INTEGERS -> "integers.txt";
                case FLOATS -> "floats.txt";
            };
        }
    }
    private static class Info {
        public InfoType type;

        //Strings
        private int countStr;
        private int minLngthStr, maxLngthStr;

        //Integers
        private int countInt;
        private int minInt, maxInt, sumInt;
        private float averageInt;

        //Floats
        private int countFl;
        private float minFl, maxFl, sumFl;
        private float averageFl;

        public Info() {
            type = InfoType.NONE;
        }

        public void editInfo(String str, FileType type) {
            switch (type) {
                case STRINGS: {
                    countStr++;

                    if (countStr == 1 || minLngthStr > str.length()) {
                        minLngthStr = str.length();
                    }
                    if (countStr == 1 || maxLngthStr < str.length()) {
                        maxLngthStr = str.length();
                    }

                    break;
                }
                case INTEGERS: {
                    countInt++;
                    int value = Integer.parseInt(str);

                    sumInt += value;
                    averageInt = (float) sumInt / countInt;

                    if (countInt == 1 || minInt > value) {
                        minInt = value;
                    }
                    if (countInt == 1 || maxInt < value) {
                        maxInt = value;
                    }

                    break;
                }
                case FLOATS: {
                    countFl++;
                    float value = Float.parseFloat(str);

                    sumFl += value;
                    averageFl = sumFl / countFl;

                    if (countFl == 1 || minFl > value) {
                        minFl = value;
                    }
                    if (countFl == 1 || maxFl < value) {
                        maxFl = value;
                    }

                    break;
                }
            }
        }

        private String getShortInfo() {
            return "Количество строк: " + countStr +
                    "\nКоличество целых чисел: " + countInt +
                    "\nКоличество чисел с плавающей запятой: " + countFl;
        }

        private String getLongInfo() {
            String str = getShortInfo();

            if (countStr != 0) {
                str += "\n\t\t||Строки||" +
                        "\nСамая длинная строка: " + maxLngthStr +
                        "\nСамая короткая строка: " + minLngthStr;
            }

            if (countInt != 0) {
                str += "\n\t\t||Целые Числа||" +
                        "\nСамое большое число: " + maxInt +
                        "\nСамое маленькое число: " + minInt +
                        "\nСумма: " + sumInt +
                        "\nСреднее: " + new DecimalFormat("#.#").format(averageInt);
            }

            if (countFl != 0) {
                str += "\n\t\t||Числа с плавающей запятой||" +
                        "\nСамое большое число: " + maxFl +
                        "\nСамое маленькое число: " + minFl +
                        "\nСумма: " + sumFl +
                        "\nСреднее: " + averageFl;
            }

            return str;
        }

        @Override public String toString() {
            return switch (type) {
                case SHORT -> getShortInfo() + "\n";
                case FULL -> getLongInfo() + "\n";
                case NONE -> "";
            };
        }
    }


    public static String filter(String[] args) {
        Filter filter = new Filter();

        String[] files;
        try {
            int idx = Integer.parseInt(filter.initParam(args));
            files = filter.getFiles(args, idx);
        } catch (NumberFormatException e) {
            return filter.outInfo;
        }

        if (files == null)
            return filter.outInfo + "Нет данных для записи.";


        for (FileType type: FileType.values()) //инициализация выходных данных
            filter.outText.put(type, "");


        ArrayList<BufferedReader> readers = new ArrayList<>();
        for (String file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                readers.add(reader);
            } catch (IOException ignore) {}
        }

        filter.fileFilter(readers);

        if (filter.createOutFiles())
            return filter.outInfo + filter.info;
        else
            return filter.outInfo;
    }

    //Инициализация параметров
    private String initParam (String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o": {
                    if (i >= args.length-1) {
                        addError("Параметр команды '+' отсутствует." +
                                " Выходные файлы будут помещены в папку с программой.", args[i]);
                        break;
                    }

                    pathToFiles = args[++i] + (args[i].endsWith("\\") ? "" : "\\");

                    File path = new File(pathToFiles);
                    if (!path.exists()) {
                        if (isThereWrongChars(path.getName())) {
                            addError("В '+' есть запрещенные символы для создания папки." +
                                    " Выходные файлы будут помещены в папку с программой.", pathToFiles);
                            pathToFiles = "";
                            break;
                        }
                        if (!path.mkdir()) {
                            addError("Пути '+' не существует." +
                                    " Выходные файлы будут помещены в папку с программой.", path.getParent());
                            pathToFiles = "";
                        }
                    }
                    break;
                }
                case "-p": {
                    if (i >= args.length-1) {
                        addError("Параметр команды '+' отсутствует." +
                                " Выходные файлы будут иметь стандартные названия.", args[i]);
                        break;
                    }
                    prefix = args[++i];

                    if (isThereWrongChars(prefix)) {
                        addError("В '+' есть запрещенные символы для создания файла." +
                                " Выходные файлы будут иметь стандартные названия.", prefix);
                        prefix = "";
                    }

                    break;
                }
                case "-a": {
                    isAddData = true;
                    break;
                }
                case "-s": {
                    info.type = InfoType.SHORT;
                    break;
                }
                case "-f": {
                    info.type = InfoType.FULL;
                    break;
                }
                default: {
                    return String.valueOf(i);
                }
            }
        }

        addError("Не указаны исходные файлы.", null);
        return null;
    }

    //Чтение файлов и распределение данных
    private void fileFilter(ArrayList<BufferedReader> readers) {
        while(!readers.isEmpty()) {
            for (int i = 0; i < readers.size(); i++) {
                try {
                    String str = readers.get(i).readLine();
                    if (str == null) {
                        readers.remove(i).close();
                        i--;
                        continue;
                    }

                    try {
                        Integer.parseInt(str);
                        addStr(str, FileType.INTEGERS);
                        continue;
                    } catch (NumberFormatException ignore) {}

                    try {
                        Float.parseFloat(str);
                        addStr(str, FileType.FLOATS);
                        continue;
                    } catch (NumberFormatException ignore) {}

                    addStr(str, FileType.STRINGS);

                } catch (IOException ignore) {}
            }
        }
    }

    //Создание выходных файлов и запись фильтрованных данных
    private boolean createOutFiles() {

        for (FileType type: FileType.values()) {
            if (outText.get(type).isEmpty()) {
                outText.remove(type);
                continue;
            }
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(pathToFiles + prefix + type, isAddData));
                w.write(outText.get(type));
                w.close();
            } catch (IOException e) {
                return false;
            }
        }
        if (outText.isEmpty()) {
            addError("Нет данных для записи.", null);
            return false;
        } else
            return true;
    }



    //Доп. функции

    //Добавление данных
    private void addStr(String str, FileType type) {
        outText.replace(type, outText.get(type), outText.get(type) + str + "\n");

        info.editInfo(str, type);
    }

    //Проверка на запрещенные символы
    private boolean isThereWrongChars(String str) {
        if (str == null)
            return true;

        for (char ch: new char[]{'\\','/',':','*','?','"','<','>','|'}) {
            if (str.indexOf(ch) != -1) { return true; }
        }
        return false;
    }

    //Выделение путей исходных файлов в массив
    private String[] getFiles(String[] args, int fromIdx) {
        ArrayList<String> files = new ArrayList<>();
        for (int i = fromIdx; i < args.length; i++) {
            String[] arg = args[i].split(",");
            if (arg.length == 1) {
                if (!new File(args[i]).exists())
                    addError("Файл '+' не возможно прочитать или его не существует.", args[i]);
                else
                    files.add(args[i]);
                continue;
            }

            File folder = new File(new File(arg[0]).getParent());
            if (!folder.exists()) {
                addError("Пути '+' не существует.", folder.getName());
                continue;
            }

            for (int j = 0; j < arg.length; j++) {
                String filePath = j==0 ? arg[0] : folder.getPath() + "\\" + arg[j];
                if (!new File(filePath).exists()) {
                    addError("Файл '+' не возможно прочитать или его не существует.", filePath);
                    continue;
                }

                files.add(filePath);
            }
        }

        return files.isEmpty() ? null : files.toArray(new String[0]);
    }

    //Добавление ошибки
    private void addError(String error, String param) {
        if (error.contains("+") && param != null && !param.isEmpty())
            outInfo += error.replace("+", param) + "\n";
        else
            outInfo += error + "\n";
    }
}