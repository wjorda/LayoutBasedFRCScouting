# Layout Based FRC Scouting
An Android application for scouting FIRST Robotics Competition events. Uses XML based configuration files to create 
different scoring panels for each year's game.

### Running this code
This code is not distributed with a stable APK. To build one, clone the repository into Android Studio and compile.

### XML Config Files
This application uses XML-based config files to build different scoring panels. Here is an example:
```
  <?xml version="1.0" encoding="UTF-8"?>
  <scouting>
    <group name="Counters"/>
    <counter id="counter1" name= "Counter 1" initValue = "0"/>
    <counter id="counter2" name= "Counter 2" initValue = "50"/>
    <group name="Checkboxes"/>
    <checkbox id="checkbox1" name= "Checkbox 1" />
    <checkbox id="checkbox2" name= "Checkbox 2" />
    <group name="Ratings"/>
    <rating id="rating1" name="3-Star Rating" scale = "3" />
    <rating id="rating2" name="6-Star Rating" scale = "6" />
    <rating id="rating2" name="10-Star Rating" scale = "10" />
    <group name="Notes"/>
    <notes id="notes" hint="Enter notes about things here!" />
  </scouting>
```

### CSV Schedule Files
This application utilizes CSV files in order to create match schedules. A sample CSV file, generated from the 
schedule of the 2014 Chesapeake Regional is located below:
```
Match, Red1, Red2, Red3, Blue1, Blue2, Blue3
1, 4464, 229, 3389, 4949, 686, 1748
2, 2528, 2964, 2537, 329, 2819, 5115
3, 1885, 3941, 3650, 3714, 1547, 4945
4, 2199, 3748, 53, 1601, 4505, 1446
5, 3793, 401, 4514, 620, 1389, 3283
6, 888, 2534, 4541, 1629, 63, 2849
7, 5106, 1111, 1, 4464, 229, 3389, 4949, 686, 1748
2, 2528, 2964, 2537, 329, 2819, 5115
3, 1885, 3941, 3650, 3714, 1547, 4945
4, 2199, 3748, 53, 1601, 4505, 1446
5, 3793, 401, 4514, 620, 1389, 3283
6, 888, 2534, 4541, 1629, 63, 2849
7, 5106, 1111, 1719, 3373, 1195, 1610
8, 4137, 2421, 623, 2377, 233, 4067
9, 2363, 4456, 1741, 2483, 4638, 5269
10, 2964, 1885, 2199, 3941, 620, 3389
11, 2537, 3650, 329, 4514, 1748, 1629
12, 4945, 1601, 888, 3793, 5106, 229
13, 1195, 3714, 2528, 4464, 3373, 1446
14, 63, 1389, 623, 4505, 2483, 1111
15, 5115, 4541, 3283, 2421, 2363, 233
16, 4067, 53, 686, 401, 2819, 5269
17, 2377, 1719, 4137, 4949, 1741, 2534
18, 4638, 4456, 3748, 1610, 1547, 2849
19, 2483, 4945, 4514, 1446, 888, 1629
20, 5106, 620, 1885, 4505, 4541, 623
21, 4464, 1389, 1748, 2964, 53, 233
22, 3389, 1601, 329, 2377, 1195, 401
23, 3373, 1547, 3283, 686, 2537, 5115
24, 3714, 4137, 2199, 2819, 2421, 1610
25, 2528, 3650, 229, 4456, 1719, 63
26, 2363, 4949, 1111, 1741, 3748, 4067
27, 2849, 3941, 4638, 3793, 2534, 5269
28, 2483, 3373, 53, 4541, 2377, 4514
29, 3283, 4464, 401, 1446, 4137, 1885
30, 4945, 63, 233, 620, 1547, 44561719, 3373, 1195, 1610
```

### Libraries Used
######[Java CSV](http://www.csvreader.com/java_csv.php) by Bruce Dunwiddie
######[aFileDialog](https://code.google.com/p/afiledialog/) by Jose Maldonado
