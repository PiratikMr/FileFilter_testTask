import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Filter {
    private String pathToFiles = "";
    private String prefix = "";
    private final Info info = new Info();
    private boolean isAddInfo = false;

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
        public int countStr;
        public int minLngthStr, maxLngthStr;

        //Integers
        public int countInt;
        public int minInt, maxInt, sumInt;
        public float averageInt;

        //Floats
        public int countFl;
        public float minFl, maxFl, sumFl;
        public float averageFl;

        public Info() {
            type = InfoType.NONE;
        }

        private String getShortInfo() {
            return "Количество строк: " + countStr +
                    "\nКоличество целых чисел: " + countInt +
                    "\nКоличество чисел с плавающей запятой: " + countFl;
        }

        private String getLongInfo() {
            String str = getShortInfo();
            DecimalFormat df = new DecimalFormat("#.#");
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
                        "\nСреднее: " + df.format(averageInt);
            }

            if (countFl != 0) {
                str += "\n\t\t||Числа с плавающей запятой||" +
                        "\nСамое большое число: " + maxFl +
                        "\nСамое маленькое число: " + minFl +
                        "\nСумма: " + sumFl +
                        "\nСреднее: " + df.format(averageFl);
            }

            return str;
        }

        @Override public String toString() {
            return switch (type) {
                case SHORT -> getShortInfo();
                case FULL -> getLongInfo();
                case NONE -> "";
            };
        }
    }


    public static String filter(String[] args) {
        Filter filter = new Filter();
        String str = filter.initParam(args);
        int idx;
        try {
            idx = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return str;
        }

        for (FileType type: FileType.values()) {
            filter.outText.put(type, "");
        }


        ArrayList<BufferedReader> readers = new ArrayList<>();
        for (int i = 0; i < args.length - idx; i++) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(args[idx+i]));
                readers.add(reader);
            } catch (IOException e) {
                filter.outInfo += "Файл '" + args[idx+i] + "' не возможно прочитать или его не существует.\n";
            }
        }

        filter.fileFilter(readers);

        if (filter.createOutFiles())
            return filter.outInfo + filter.info;
        else
            return filter.outInfo;
    }

    private String initParam (String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o": {
                    if (i >= args.length-1) {
                        return "Параметр команды '-o' отсутствует.";
                    }
                    pathToFiles = args[++i] + "\\";
                    break;
                }
                case "-p": {
                    if (i >= args.length-1) {
                        return "Параметр команды '-p' отсутствует.";
                    }
                    prefix = args[++i];

                    if (isThereWrongChars(prefix)) {
                        return "В '" + prefix + "' есть запрещенные символы для создания файла.";
                    }

                    break;
                }
                case "-a": {
                    isAddInfo = true;
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

        return "Не указаны входные файлы.";
    }

    private void fileFilter(ArrayList<BufferedReader> readers) {
        while(!readers.isEmpty()) {
            for (int i = 0; i < readers.size(); i++) {
                String str;
                try {
                    str = readers.get(i).readLine();
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

    private boolean createOutFiles() {
        for (FileType type: FileType.values()) {
            if (outText.get(type).isEmpty()) continue;
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(pathToFiles + prefix + type, isAddInfo));
                w.write(outText.get(type));
                w.close();
            } catch (IOException e) {
                outInfo += "Пути '" + pathToFiles + "' не существует.\n";
                return false;
            }
        }
        return true;
    }



    private void addStr(String str, FileType type) {
        outText.replace(type, outText.get(type), outText.get(type) + str + "\n");

        switch (type) {
            case STRINGS: {
                info.countStr++;

                if (info.countStr == 1 || info.minLngthStr > str.length()) {
                    info.minLngthStr = str.length();
                }
                if (info.countStr == 1 || info.maxLngthStr < str.length()) {
                    info.maxLngthStr = str.length();
                }

                break;
            }
            case INTEGERS: {
                info.countInt++;
                int value = Integer.parseInt(str);

                info.sumInt += value;
                info.averageInt = (float) info.sumInt / info.countInt;

                if (info.countInt == 1 || info.minInt > value) {
                    info.minInt = value;
                }
                if (info.countInt == 1 || info.maxInt < value) {
                    info.maxInt = value;
                }

                break;
            }
            case FLOATS: {
                info.countFl++;
                float value = Float.parseFloat(str);

                info.sumFl += value;
                info.averageFl = info.sumFl / info.countFl;

                if (info.countFl == 1 || info.minFl > value) {
                    info.minFl = value;
                }
                if (info.countFl == 1 || info.maxFl < value) {
                    info.maxFl = value;
                }

                break;
            }
        }
    }

    private boolean isThereWrongChars(String str) {
        for (char ch: new char[]{'\\','/',':','*','?','"','<','>','|'}) {
            if (str.indexOf(ch) != -1) { return true; }
        }
        return false;
    }
}