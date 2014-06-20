 Congratulations! You are now the proud owner of a Gregory Loden Run-class autoclicker!

 This wonderful piece of software allows you to write your own mouse-controlling programs using text
files and a simple syntax, allowing for easy and quick program creation.
 To start off, create a folder named "scripts" in the same directory as the run.class file. This is
where all autoclick scripts will live. To be recognized, all scripts should end with .txt.





	On to how to use the program!





 To run a script, type "java run ??? ..." in a command-line terminal. This will run the script named
???. The ... is an arbitrary amount of arguments to be passed to the autoclicker for use in the
script. We'll see why we need these later.
 In all commands, command calls and arguments are separated by whitespace tabs or spaces. Tabs and
spaces are recognized by the program as separators.
 The program supports a variety of commands, the number of which will grow as the program does.



	Let's begin!



 The core of any autoclicker is the ability to perform right clicks, left clicks, mouse moving, and
pausing.
 In this autoclicker, a command looks like this:

	left	[x]	[y]

 When the autoclicker's line pointer reaches a line, the command on that line is executed.
 In this case, a left click. The name goes at the beginning, followed by the variables. In this
README file, anything, within [] brackets stands for something that would get replaced by actual
values written by the scriptmaker. In the left click, the mouse gets moved to the x-[x] and y-[y]
coordinates provided, and left clicks that spot. For example:

	left	100	200

will left-click at x-100, y-200.
 All commands take up one line only, and contain only lowercase letters.
 Right click:

	right	[x]	[y]

 Move:

	move	[x]	[y]

 Right will move and perform a right click, and move will move the mouse without clicking. Any
number of tabs and spaces can be added between arguments and commands, and the interpreter will pass
right over them. That means that

	move	[x]	[y]

is the same as

	   move			[x] [y]

but not

	move	[x][y]

as whitespace is needed to separate arguments. Here, the interpreter would interpret the two numbers
as one bigger number.
 Run also supports pausing for certain periods of time. The command

	pause	[t]

will pause for [t] milliseconds. For instance,

	pause	1000

will have the autoclicker do nothing for 1 second. There are 2 more types of pauses:

	pause	[x]	[y]	[r]	[g]	[b]

and

	pausenot	[x]	[y]	[r]	[g]	[b]

 These pause until something happens to the color of the pixel at x-[x] and y-[y]. Pause will pause
until the red-green-blue color at x-[x], y-[y] is red-[r], green-[g], and blue-[b]. Pausenot does
the opposite- it will pause WHILE the pixel is that color. The command

	pause	123	456	128	255	32

will stop the autoclicker until the pixel at x-123, y-456 is the RGB color r-128, g-255, b-32


 And of course, we can't forget looping! For this, 2 commands are provided:

	jump	[LABEL]

will move the line pointer to a label in the format

	:[LABEL]

 For example:

	:Endless
	jump	Endless

is an infinite loop. Here's a cumulative example:

	:main
	right		100	200
	pause		110	210	255	255	255
	left		110	210
	pausenot	110	210	255	255	255
	move		90	190
	pause		500
	jump		main

 This will right click at x-100, y-200, wait until the pixel at x-110, y-210 is white (r-255, g-255,
b-255), left click  at x-110, y-210, wait for it to stop being white, move the mouse to x-90, y-190,
wait half a second, and repeat.
 Run will not perform any automatic looping. If the jump were removed, the code would end after the
last pause.

 The autoclicker can also run scripts from within a script:

	run	[script]	...

will run the script [script] as if it was typed on the command line with the arguments ... like a
regular command-line run execution. Scripts cannot have spaces in the name.



	This is all well and good, but what programming language of any type could possibly pretend
	to be complete without variables?



 Not this one! You can define variables with this command:

	create	[type]	[name]	[value]

 This will create a variable of type [type] called [name] with starting value [value]. These will
probably be of type int, used to base clicks on other data, but they can also be booleans and
colors. All variables are for use only within the active script. Scripts run from within that script
will not be able to use those variables.
 Here are some variable creation examples:

	create	int	x	100
	create	int	z	random	30
	create	boolean	Good?	true
	create	color	white	255	255	255
	create	color	brown	at	123	456
	create	int	y	x
	create	boolean	Good?2	Good?
	create	color	brown2	brown

 Let's go over these examples.

 In the first example, the variable x was created as an integer. To create an integer, the only
argument supplied is the value to store to it.
 In the second example, w was created with a random value. Using the word "random" as the first
argument will assign the variable to a number ranging from 0 to the second argument - 1. In this
case, z would be assigned a number from 0 to 29 inclusive.
 In the third example, Good? is created as a boolean. Just like integers, the only argument is the
value stored to it, and you can put other variables in place of the argument.
 In the fourth example, white was created as a color, which is used for a special command. This is
one of three ways to make a color, both of which take three arguments. The first way, used to create
white, is to give the red, green, and blue values of a color, in that order.
 In the fifth example, brown demonstrates the second way to create a color: the arguments are the
word "at", followed by two more arguments- the x and y coordinates of a pixel location. The value
stored to the variable is the color at that pixel.
 In the sixth, seventh, and eigth examples, variables were created using other variables as a single
argument. This will create a variable with the value of the argument variable. In addition to
copying variables, they can also be used as regular arguments. This command,

	create	color	brown	x	y	z

would create a color with a red value of 100, green value of 100, and blue value between 0 and 29.
 So now that we have variables, we can use them in other commands. Using the above variable
assignments, the command

	left	x	y

would left click at x-100, y-100.

 Creating multiple variables with names that are used elsewhere will not cause an error, but may
cause problems when running the script. Be careful when naming variables.

 In addition to creating variables, variables already created can be set to new values like this:

	set	[name]	[value...]

 Setting variables works just like creating variables, for all types of variables, except you don't
need a variable type. Set changes the variable of name [name] to the value that the arguments
[value...] produce. Variables can also technically be changed with create, as the interpreter hit
the most recent entry first, but this is generally not recommended.
 Here is an example of set:

	set	x	200

 Assuming the same variable assignments, this would change x's value to be 200. So now,

	left	x	y

would left click at x-200, y-100. If a variable has not been created when set is called, set will
not make a new variable. This means that

	set	z	100

would do nothing to any variables, since z has not been created.
 There is also a special set command for using arguments to the script:

	setargto	[prefix]	[name]

 This works almost as if you called

	set	[name]	[value]

assuming that [prefix][value] is one argument, and is a single string of characters without spaces
or tabs.
 As an example, say you had a script called "test" and you typed this on the command line:

	java run test abc123

 If you had

	create	int	doremi	0
	setargto	abc	doremi

within that script, it would end up changing doremi as if you called

	set	doremi	123

which sets the variable to 123, omitting the abc in the argument.
 Now, since some of the variable creations take in multiple arguments, but input arguments cannot
include tabs or spaces, you will have to type commas between them when typing them on the command
line. This means that arguments should be typed like "alphaat,123,456", "beta255,255,255", and
"gammarandom,30" for the following commands:

	create	color	test1	0	0	0
	create	color	test2	0	0	0
	create	int	test3	0
	setargto	alpha	test1
	setargto	beta	test2
	setargto	gamma	test3

 If you call setargto and there was no argument that started with [prefix], nothing will happen.


	Now what good is a variable you can't change?


 Run couldn't tell you, because you CAN change variables, integers at least! There are 4 change
commands in run:

	+=	[name]	[value]
	-=	[name]	[value]
	*=	[name]	[value]
	/=	[name]	[value]

 These will respectively add, subtract, multiply, and divide the value of [name] by [value], and
store the result back in [name]. Here's an example:

	create	y	1
	+=	y	1
	*=	y	-3
	-=	y	2
	/=	y	-8

 This will create a variable y equal to 1, add 1 to it to give it a value of 2, multiply it by 3 to
give it a value of -6, subtract 2 to set it to -8, and divide it by -8 to set it back to 1. All
operators work like most programming languages with 32-bit integers: division will result in an
integer rounded down, overflows will make the number go crazy.
 There are some special system variables that can be used. The variables

	.mousex
	.mousey

represent the mouse's current x- and y-coordinates. So for example,

	create	int	clickx	.mousex
	create	int	clicky	.mousey

would create two variables, each with a mouse coordinate. If the mouse was at x-145, y-234, clickx
would have a value of 145 and clicky would have a value of 234.
 There is also a timer variable,

	.timer

that tracks how many milliseconds have passed since the start of the program, or since the timer was
last reset. You can reset it to 0 with the command

	treset

 Variables created by the user cannot have a . at the beginning of the name. The autoclicker will
not run if you try to do this.



	So now we have autoclicking and variables. What more could we possibly want?



 Conditionals and for loops, that's what!
 Earlier, it was mentioned that you could create variables of type boolean. The main use of these
is for the if commands that look like this:

	if	[boolean]
		[command1]
	[command2]

 The way this works is that if the boolean is false, the next line gets skipped. You can also view
it like this: if and only if the boolean is true, then the line next gets called. So if [boolean]
is true, both [command1] and [command2] get called. If [boolean] is false, [command1] doesn't get
called. These can be paired with jumps for more complex if conditionals:

	create	boolean	test	true
	create	int	x	100
	create	int	y	100
	if	test
		jump	thisisalabel
	set	x	150
	jump	end
	:thisisalabel
	set	y	150
	jump	end
	:end

 This will set y to 150 is test is true, and set x to 150 if x is false.
 Stored booleans are not the only booleans that can be put in an if clause. Ifs have multiple forms
with different comparisons. Here is one form:

	if	input	[arg]

 This evaluates to true if [arg] was given as an argument to the program. For instance, calling

	java run thisisascript hello

would make "hello" be an argument. These ifs,

	if	input	goodbye
	if	input	hello
	if	input	he
	if	input	helloo

would evaluate to false, true, false, and false respectively.
 There are three integer evaluation clauses:

	if	==	[value1]	[value2]
	if	>=	[value1]	[value2]
	if	>	[value1]	[value2]

 These are pretty straightforward, they  evaluate to true if [value1] is equal to, greater-than-
or-equal-to, or greater than [value2] respectively.
 You can also have a not clause:

	if	not	[clause]

 This will skip the line if [clause] IS true. For example,

	create	boolean	beta	false
	if	not	beta
		set	beta	true

will end up setting the value of beta to true, since beta is NOT true.
 You can nest nots, so two nots in a row will be treated like no nots, 3 nots is like 1, and so on,
but only nots at the beginning of the clause will be considered.
 The last form,

	if	colorat	[x]	[y]	[r]	[g]	[b]

evaluates to true if the pixel at x-[x], y-[y] is r-[r], g-[g], b-[b]. There is also

	if	colorat	[x]	[y]	[color]

which uses the color variables mentioned earlier.
 Of course, you can give ifs a standard true or false and it will get recognized.

 Now on to for loops. A for loop has this form:

	for	[name]	[init]	[max]	[incr]

and ends with the command

	end

 The for loop generates a variable named [name] with a value of [init]. When the code reaches the
end command, it increases [name] by [incr] and jumps back to the top. If the variable is greater
than [max], the pointer jumps to the line after the end command. As for the variables within the for
loop, they take on the following properties:
- The variables defined by the for loop are available to all commands within that loop's for/end
    commands.
- If there is a variable outside the for loop with the same name as that variable, the for loop one
    will get picked if it gets referenced.
- Variables only exist within the for loop: once the for loop ends, the variables are no longer
    available.
- Nested for loops have access to all variables from the previous for loops.
- Variables created with create commands are global, and will not disappear.
 In addition, the jump command cannot jump to labels outside of the for loop.

 Paired with for loops is the ability to exit them early. This is done with 3 breaking commands of
varying power:

	break
	return
	quit

 Break will exit out of the current loop, or exit out of the current script if it was not within a
loop. Return will exit out of the current script but the program will keep running if another script
called it. Quit will stop all autoclicking and end the program.

 In addition to clicking, run also allows you to type. There are three commands for key presses:

	type	[text]
	press	[button]
	release	[button]

 Type will type all remaining characters on the line, tabs spaces and all, uppercase and lowercase
letters. Press will press down one of the following buttons when substituting [button]:

	enter
	backspace
	delete
	control
	up
	down
	left
	right
	shift
	alt
	capslock
	escape

 Release will release the key.

 One last feature, you can print lines almost just like you can in java. The command

	print	[text]

will print everything after the print- tabs, spaces and all;

	printval	[name]

will print out the value of the variable named [name], and

	println

will print an empty line. Here's an example:

	create	int	test	123
	print	The value of "test" is 
	printval	test
	print	 , and here's a tab:	.
	println

 This would print out this line:

The value of "test" is 123 , and here's a tab:	.

 Notice the spaces at the end of the first print line, and at the beginning of the second one. Tabs
and spaces get treated like regular characters until the end of the line.



 And that's that! You now know how to create autoclicker scripts! You can also leave comments by
starting a line with //, or pretty much anything except one of the commands. The autoclicker will
also automatically check the syntax of all scripts to be run. Any script can be stopped at any time
by moving the mouse.

 Run is a growing program, there are always plenty of things that can be added or changed. Feel free
to send an e-mail with suggestions, misspellings, confusions, or bug reports.