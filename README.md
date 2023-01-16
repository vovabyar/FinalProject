# The final task of industrial programming.

An application has been implemented, which consists of two parts (** UI implementation and console implementation **).

## Console application features:
### Description:
1. A file is received as input. The application automatically determines for the input file (for example, `test.txt.encrypted.encrypted` or `test.json.zip`), then the decryption and unzipping process begins until the source file with extensions  `.txt`, `.json`, `.xml` 
  - **IMPORTANT: the application supports cases of multi-level nesting of encryption & archiving archives, for example, `test.txt.encrypted.zip.encrypted.encrypted.encrypted.zip.encrypted` etc**;
2. Processes the received information by searching for arithmetic expression tokens in any of the `.txt`, `.json`, `.xml` files;
3. Handles **any arithmetic expression in brackets** with any complexity (within **double** (`4.9e-324 to 1.7e+308`), also taking into account the division by 0 operation.
3. Writes data to the output file in one of the types `.txt`, `.json`, `.xml` depending on the input file;
 
 
**The following Design Patterns are used in the console application:** `Strategy` (to select the decryption/unarchiving strategy depending on the extension), `Singleton`.
 
### `FileEncryption.java`

**An utility that encrypts a file using AES encryption.**

#### **Technical details**

This application allows you to encrypt a file using the [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) encryption algorithm, asking you for a password to use it as its key. During the file transformation process, it's guaranteed that no data is lost: It's never the original file that is encrypted but a copy of it.

#### **Functionalities**

* File encryption (or safely cancel it without crashing the application)
* File decryption (or safely cancel it without crashing the application)
* Friendly graphical user interface showing the progress of the task

### `ZipUtility.java`

**ZipArchive is a simple utility class for zipping and unzipping files on windows, linux.**

- Unzip zip files;
- Create zip files;
- Create large archive files;

### Libraries used in the project:
1. `googlecode.json.simple` - for working with `.json` files and `json` objects.
2. `junit-jupiter-5.8.1` - for working with unit tests.



## GUI part application (based on JavaFX library):
Why JavaFX? Swing is a legacy library that fully features and provide pluggable UI components, whereas JavaFX has UI components that are still evolving with a more advanced look and feel.

Application demo:

![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/ezgif.com-gif-maker.gif)

The application interface consists of the main menu (closing the application, support for other languages), a slide-out menu of operations, TableView and a built-in editor for `.txt`, `.json`, `.xml` files.

### The main menu consists of a MenuBox, a close button and a button for adding a new tab for parallel work with files:
![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/2.png)

### The "moving out" menu of operations consists of the following operations:
1. The operation of opening a file through the built-in editor.
2. Getting the final `.txt`, `.json`, `.xml` file.
3. Unzipping operation.
4. Archiving operation.
5. Decryption operation.
6. Encryption operation.
7. For the convenience of working with files, the "Delete" button works, which deletes the file.

![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/1.png)

It is worth noting that when you call this menu, the rest of the application area is darkened.

### TableView:

Nicely styled tableView.

![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/3.png)

### Built-in editor for `.txt`, `.json`, `.xml` files:

There are save and open functions. ProgressBar showing the progress of opening a file. Beautiful backlight.

![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/4.png)

### Libraries used in this part:
1. `javafx 18`
2. `javafx.animation` - for working with animations.
3. `junit-jupiter-5.8.1` - for working with unit tests.

## And of course, you can't create such a beautiful design without CSS:

![](https://github.com/vovabyar/FinalProject/blob/master/screenshots/5.png)
