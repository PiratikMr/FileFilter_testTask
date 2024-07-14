# Инструкция 

**Версия Java**: 21.0.2  

Для запуска программы нужно запустить исполняемый файл `util.jar` через консоль.

```
java -jar util.jar "ваши аргументы"
``` 


# Возможные ошибки и их обработка программой

### 1. Нет аргументов в виде исходных файлов (названия файлов)    

<p>Если в аргументах не даны названия исходных файлов, то в консоле появляется текст-оповещение об этом в виде</p>

```
Не указаны входные файлы.
```

**Критическая ошибка.** Программа не выпоняется, так как нет данных для фильтрации.</p>

### 2. Нет аргументов в виде параметров после параметрических команд `-o` и `-p`   

<p>Следующий аргумент, стоящий за одной из данных команд, будет являтся аргументом-параметром для этой команды.
  При записи одной из этих команд последним аргументом, в консоле появится текст-оповещение об этом в виде</p>
  
```
Параметр команды '-o' отсутствует.
Параметр команды '-p' отсутствует.
```

<p>Ошибка от обстоятельств. Программа выполняется при наличии аргумента-параметра. Программа не выполняется
при отсутсвии аргумента-параметра во избежания сохранения выходных файлов в корневую папаку программы или 
со стандартным названием.</p>

### 3. В исходных файлах присутсвуют другие типы данных помимо `Integer` и `Float`

Все типы данных, не относящиеся к типу `Integer` и `Float`, будут считаться типом `String` и записываться
  в соответсвующий файл `strings.txt`.

<p>Не критическая ошибка. Программа выполняется.</p>

### 4. Неверно указан путь / название исходного файла или файла не сущетсвует

<p>В консоле появится текст-оповещение об этом в виде</p>

```
Файл 'fileName.txt' не возможно прочитать или его не существует.
```

<p>Не критическая ошибка. Программа выполняется, пропуская данный тип файлов.</p>

### 5. Порядок аргументов нарушен: сначала исходные файлы, затем команды

<p>Программа проигнорирует команды после аргументов-названий файлов и будет считать эти команды названиями исходных файлов.
Что повлечет за собой предыдущую ошибку.</p>

<p>Не критическая ошибка. Программа выполняется, не учитывая команды, стоящие после аргументов-названий файлов.</p>

### 6. Путь для выходных файлов указан неверно / не существует.

<p>Если путь подразумевает создание новой папки, то программа создаст папку, но не больше одной, и сохранит туда файлы. Если на пути нужно создать больше одной папки, то
в консоле появится текст-оповещение об этом в виде</p>

```
Пути 'path_to_files' не существует.
```

**Критическая ошибка.** Программа не выполняется во избежания сохранения файлов в корнувую папку программы.

### 7. В префиксе или пути сохранения присутсвуют символы, которые нельзя использовать в названии файла / папки в Windows (`\`,`/`,`:`,`*`,`?`,`"`,`<`,`>`,`|`).

<p>В консоле появится текст-оповещение об этом в виде</p>

```
В 'prefix_name' есть запрещенные символы для создания файла.
В 'path_to_file' есть запрещенные символы для создания папки.
```

**Критическая ошибка.** Программа не выполняется, так как не возможно создать файлы с названием, содержащие такие символы.

### 8. В аргументах присутсвуют противоречащие команды.

Противореачащие команды - команды которые протеворечат сами себе. Например: в аргументах находится две команды `-f` и `-s`, что означает вывод и полной статистики и не полной.
В таких случаях команда, написанная позже, будет исполняемой. То есть, при наборе таких аргументов

```
-s -a -f in.txt
```

команда `-f` будет учтена, а команда `-s` будет проигнорирована.   

<p>Не критическая ошибка. Программа выполняется, учитывая команды, написанные позже.</p>

### 9. Исходные файлы пустые или их все не возможно прочитать.

Если нет данных для записи, а это может быть связано с пустыми исходными файлами, их отсутствием или не возможностью их прочитать, в консоле появится текст оповещение об этом в виде

```
Нет данных для записи.
```

<p>Не критическая ошибка. Программа выполняется, но не создает выходные файлы, так как нет данных для записи.</p>

### 10. Исходные файлы могут содержать пустые строки.

<p>Пустая строка является строкой. Поэтому пустые строки будут относится к категории строк и не будут игнорироваться.</p>   

<p>Не критическая ошибка. Программа выполняется</p>  
  