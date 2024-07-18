# Инструкция 

**Версия Java**: 21.0.2  

Для запуска программы нужно запустить исполняемый файл `util.jar` через консоль.

```
java -jar util.jar "ваши аргументы"
``` 

# Дополнительные функции

### 1. Сокращенный путь к исходным файлам

Если нужно указать путь к нескольким файлам, находящимся в одном месте, то можно указать полный путь только к одному из этих файлов
и через `,` указать названия других файлов без пробелов, находящихся в этом же расположении. Если исходные файлы находятся в папке с программой,
то можно просто написать названия файлов через `,`.

Вместо такой записи

```
path\to\files\in1.txt path\to\files\in2.txt path\to\files\in3.txt
```

можно использовать такую

```
path\to\files\in1.txt,in2.txt,in3.txt
```

Данная запись будет считаться за один аргумент. Для указания новой директории / исходного файла нужно поставить пробел.

### 2. Нарушение порядка аргументов

В программе можно нарушать порядок аргументов. То есть, не обязательно указывать аргументы-названия исходных файлов в конце. Аргументы-команды,
стоящие после аргументов-названий файлов, будут учитываться.

Запись

```
-f -a -p prefix_ in1.txt in2.txt
```

будет эквивалентна

```
in1.txt -a in2.txt -f -p prefix_
```

### 3. Проверка на запрещенные символы в названии создаваемых папок и файлов

При создании выходных файлов и папки (если это нужно) проверяется название создаваемой папки, указанное в параметре команды `-o`, и префикс 
создаваемых файлов, указанный в параметре команды `-p`. Если в этих параметрах присутствуют (`\`,`/`,`:`,`*`,`?`,`"`,`<`,`>`,`|`), то в консоли
появится текст-оповещение об этом в виде

```
В 'prefix_name' есть запрещенные символы для создания файла. Выходные файлы будут иметь стандартные названия.
В 'path_to_file' есть запрещенные символы для создания папки. Выходные файлы будут помещены в папку с программой.
```

И исходные файлы будут сохранены в папку с программой или со стандартными названиями (без префикса).


### 4. Создание папки на пути сохранения выходных файлов

В аргументе-параметре команды `-o`, помимо указания существующего пути, можно указать название новой папки в данном пути. Программа создаст эту папку
и поместит туда выходные файлы. 


# Возможные ошибки и их обработка программой

### 1. Нет аргументов-названий исходных файлов   

В консоли появится текст-оповещение об этом в виде

```
Не указаны исходные файлы.
```

### 2. Нет аргументов в виде параметров после параметрических команд `-o` и `-p`   

<p>Следующий аргумент, стоящий за одной из данных команд, будет являться аргументом-параметром для этой команды.
При записи одной из этих команд последним аргументом, в консоли появится текст-оповещение об этом в виде</p>
  
```
Параметр команды '-p' отсутствует. Выходные файлы будут иметь стандартные названия.
Параметр команды '-o' отсутствует. Выходные файлы будут помещены в папку с программой.
```

<p>Программа выполняется, сохраняя выходные файлы в папку с программой / со стандартными названиями.</p>

### 3. В исходных файлах присутствуют другие типы данных, помимо `Integer` и `Float`

Все типы данных, не относящиеся к типу `Integer` и `Float`, будут считаться типом `String` и записываться
в соответсвующий файл `strings.txt`.


### 4. Неверно указан путь / название исходного файла или файла не существует

В консоли появится текст-оповещение об этом в виде

```
Файл 'fileName.txt' не возможно прочитать или его не существует.
```

Программа выполняется, пропуская данный тип файлов.


### 5. Путь для выходных файлов указан неверно / не существует

<p>Если путь подразумевает создание новой папки, то программа создаст папку, но не больше одной, и сохранит туда файлы. Если на пути нужно создать больше одной папки, то
в консоли появится текст-оповещение об этом в виде</p>

```
Пути 'path_to_files' не существует. Выходные файлы будут помещены в папку с программой.
```

<p>Программа выполняется, сохраняя выходные файлы в папку с программой.</p>

### 6. В префиксе или пути сохранения присутствуют символы, которые нельзя использовать в названии файла / папки в Windows (`\`,`/`,`:`,`*`,`?`,`"`,`<`,`>`,`|`)

В консоли появится текст-оповещение об этом в виде

```
В 'prefix_name' есть запрещенные символы. Выходные файлы будут иметь стандартные названия.
В 'path_to_file' есть запрещенные символы. Выходные файлы будут помещены в папку с программой.
```

<p>Программа выполняется, сохраняя выходные файлы в папку с программой / со стандартными названиями.</p>

### 7. В аргументах присутствуют противоречащие команды

Противоречащие команды - команды которые противоречат сами себе. Например: в аргументах находится две команды `-f` и `-s`, что означает вывод и полной статистики и не полной.
В таких случаях команда, написанная позже, будет исполняемой. То есть, при наборе таких аргументов

```
-s -a -f in.txt
```

команда `-f` будет учтена, а команда `-s` будет проигнорирована.   

Программа выполняется, учитывая команды, написанные позже.

### 8. Исходные файлы пустые или их все не возможно прочитать

Если нет данных для записи, а это может быть связано с пустыми исходными файлами, их отсутствием или не возможностью их прочитать, в консоли появится текст-оповещение об этом в виде

```
Нет данных для записи.
```

Программа выполняется, не создавая выходные файлы, так как нет данных для записи.

### 9. Исходные файлы содержат пустые строки

Пустая строка является строкой. Поэтому пустые строки будут относиться к категории строк и не будут игнорироваться.   

  
### 10. Исходные файлы повторяются

При повторении аргумента-названия исходного файла несколько раз в консоли появится текст-оповещение в виде

```
Файл 'file.txt' уже указан.
```

### 11. Исходные файлы имеют разрешение не `.txt`

В консоли появится текст-оповещение об этом в виде

```
Файл 'file' не возможно прочитать или его не существует.
```

### 12. Переопределенные параметры не верны

При наборе двух и более одинаковых параметрических команд будут учитываться только команды, записанные позже. Если же параметры команд, написанных последними, не подходят (пустой, не существует пути,
запрещенные символы), то берутся предыдущие подходящие параметры этой же команды, если та была набрана раньше.

Если параметрическая команда имеет не подходящий параметр (пустой, не существует пути, запрещенные символы), то при наличии этой же команды, записанной ранее с подходящим параметром,
будет учитываться ее параметр, а в консоли появится текст-оповещение в виде

`-p prefix_ -p badprefix*_ in.txt`

```
В 'badprefix*_' есть запрещенные символы для создания файла.
Выходные файлы будут начинаться с 'prefix_'.
```

Таким же образом для пути сохранения выходных файлов

`-o good/path/newFolder -p bad/path/newFolder* in.txt`

```
В 'newFolder*' есть запрещенные символы для создания папки.
Выходные файлы будут помещены в 'good/path/newFolder'.



