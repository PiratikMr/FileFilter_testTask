# Инструкция 

**Версия Java**: 21.0.2  

Для запуска программы нужно запустить исполняемый файл `util.jar` через консоль. 


# Возможные ошибки и их обработка программой

### 1. Нет аргументов в виде исходных файлов (названия файлов)    

<p>Если в аргументах не даны названия исходных файлов, то в консоле появляется текст-оповещение об этом в виде</p>

```
Не указаны входные файлы.
```

<p>Программа не выпоняется. Нет данных для фильтрации.</p>

### 2. Нет аргументов в виде параметров после параметрических команд `-o` и `-p`   

<p>Следующий аргумент, стоящий за одной из данных команд, будет являтся аргументом-параметром для этой команды.
  При записи одной из этих команд последним аргументом, в консоле появится текст-оповещение об этом в виде</p>
  
```
Параметр команды '-o' отсутствует.
Параметр команды '-p' отсутствует.
```

<p>Программа не выполняется для избежания сохранения файлов в корневую папку программы.</p>

### 3. В исходных файлах присутсвуют другие типы данных помимо `Integer` и `Float`

Все типы данных, не относящиеся к типу `Integer` и `Float`, будут считаться типом `String` и записываться
  в соответсвуйющий файл `strings.txt`.

### 4. Неверно указан путь / название исходного файла или файла не сущетсвует

<p>В консоле появится текст-оповещение об этом в виде</p>

```
Файл 'fileName.txt' не возможно прочитать или его не существует.
```

<p>Программа выполняется, пропуская данный тип файлов.</p>

### 5. Порядок аргументов нарушен: сначала исходные файлы, затем команды

<p>Программа проигнорирует команды после аргументов-названий файлов и будет считать эти команды названиями исходных файлов.
Что повлечет за собой предыдущую ошибку.</p>

### 6. Путь для выходных файлов указан неверно / не существует.

<p>В консоле появится текст-оповещение об этом в виде</p>

```
Пути 'path_to_files' не существует.
```

<p>Программа не выполняется для избежания сохранения файлов в корнувую папку программы.</p>

### 7. В префиксе присутсвуют символы, которые нельзя использовать в названии файла в Windows (`\`,`/`,`:`,`*`,`?`,`"`,`<`,`>`,`|`).

<p>В консоле появится текст-оповещение об этом в виде</p>

```
В 'prefix_name' есть запрещенные символы для создания файла.
```

<p>Программа не выполняется. Не возможно создать файлы с таким названием.</p>

### 8. В аргументах присутсвуют противоречащие команды.

Противореачащие команды - команды которые протеворечат сами себе. Например: в аргументах находится две команды `-f` и `-s`, что означает вывод и полной статистики и не полной. В таких случаях команда, написанная позже, будет исполняемой. То есть, при наборе таких аргументов

```
-s -a -f in.txt
```

команда `-f` будет учтена, а команда `-s` будет проигнорирована.
<p>Программа выполняется.</p>

### 9. Исходные файлы пустые или их все не возможно прочитать.

Если нет данных для записи, а это может быть связано с пустыми исходными файлами, их отсутствием или не возможностью их прочитать, в консоле появится текст оповещение об этом в виде

```
Нет данных для записи.
```

<p>Программа не выполняется, так как нет данных для записи в выходные файлы.</p>
