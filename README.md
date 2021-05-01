Project 3: Regular Expressions

Authors: Devyn Roth (Section 3) / Phillip Bruce (Section 2)
Class: CS361 Section 2, 3
Semester: Spring 2021

Overview:
The overview of this project was to take a regular expression and practice using recursive algorithms in order to turn that RegEx into an NFA object.

Compiling and Using:
To compile the project run this command in the top directory
javac -cp ".:./CS361FA.jar" re/REDriver.java

To run the project run this command in the top directory
java -cp ".:./CS361FA.jar" re.REDriver ./tests/p3tc1.txt

Discussion:
This project was definitely a rough one, my partner and I had allot of issues getting started. It seemed like conceptually we had the idea of the project but writing the code was a big issue. After allot of discussion and back and forth on many discord calls. Admittedly we had to talk each other off the ledge a few times and keep each other from dropping out. At 2:30am we finally had a breakthrough. After we managed to get a basis running for the code it was pretty short work before we got a working draft compiled and running. 
The biggest hold up was getNFA(). We managed to get things going by creating two stacks, one to hold the operators in the equation, and one to hold an ArrayList of the NFA States. 
One big holdup we had was a simple mistake, we didn't add the alphabets properly to the Stack so when we were creating the NFA things were not working properly.
Overall, this was a rough project but we finished it and I am confident it works properly.

Testing:
Once we ran the provided test cases, we borrowed some of the tests that a colleague posted on Piazza.