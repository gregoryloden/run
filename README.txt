 Congratulations! You are now the proud owner of a Gregory Loden Run-class autoclicker!

 This wonderful piece of software allows you to write your own mouse-controlling programs using text
files and a simple syntax, allowing for easy and quick program creation.
 To start off, there is a folder named "scripts" in the same directory as the run.class file. This
is where all autoclick scripts will live. To be recognized, all scripts should end with .txt. A
sample script has been provided.





	On to how to use the program!





 To run a script, type "java run ??? ..." in a command-line terminal. This will run the script named
???. The ... is an arbitrary amount of arguments to be passed to the autoclicker for use in the
script. We'll see why we need these later.
 In all commands, command calls and arguments are separated by whitespace tabs or spaces. Tabs and
spaces are recognized by the program as separators.
 The program supports a variety of commands, the number of which will grow as the program does.



	Let's begin!



================================================================
SECTION 1- Standard Autoclicking Features
================================================================

 The core of any autoclicker is the ability to perform right clicks, left clicks, mouse moving, and
pausing.
 In this autoclicker, a command looks like this:

	left	[x]	[y]

 When the autoclicker's line pointer reaches a line, the command on that line is executed.
 In this case, a left click. The name goes at the beginning, followed by the arguments. In this
README file, anything, within [] brackets stands for something that would get replaced by actual
values written by the scriptmaker. In the left click, the mouse gets moved to the x-[x] and y-[y]
coordinates provided, and left clicks that spot. For example:

	left	100	200

will left-click at x-100, y-200.
 All commands take up one line only, and contain only lowercase letters. You can also leave comments
by starting a line with //, or pretty much anything except one of the commands.
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

will stop the autoclicker until the pixel at x-123, y-456 is the RGB color r-128, g-255, b-32.
 These two five-argument pauses can also take a sixth argument, combining both ways of pausing. This
number, a time in milliseconds, tells the pause how long to wait before ignoring the color and
resuming the program. This is useful if the color does not change in time, but the program can catch
this and handle it. Whichever comes first, the pause will resume. This command,

	pause	123	456	255	255	255	1000

will wait for the pixel to change, but if a second goes by, it will stop pausing and resume anyway.
 You may come across a need to end your script early. For this purpose, moving the mouse during
pauses will stop the script and end the program.


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

 In addition to standard jumping, there is also a command to use a part of the script in multiple
places at once! This is done with two similar commands. The first one,

	call	[label]

does the exact same thing that jump does, but it will remember where it got called. Paired with it
is another command,

	jumpback

which will return to the most recent call command that got executed. That means that if there are
multiple calls, jumpback will go back down the list of call commands in the reverse order that they
were called. Here's an example:

	call	test
	call	test
	call	test
	jump	end
	:test
	left	100	100
	call	test2
	jumpback
	:test2
	pause	1000
	jumpback
	:end

 This will left click at x-100, y-100, pause for a second, and repeat that twice more.

 The autoclicker can also run scripts from within a script:

	run	[script]	...

will run the script [script] as if you typed

	java run [scripts] ...

on the command line with the arguments ... like a regular command-line run execution. Scripts cannot
have spaces in the name.



	This is all well and good, but what programming language of any type could possibly pretend
	to be complete without variables?



================================================================
SECTION 2- Programming Language Features- Variables
================================================================

 Yeah, you heard me! In addition to being an autoclicker, run serves as a fully functional
programming language! Nothing super-fancy, but perfect for autoclicking applications. So back to the
question: what programming language would be complete without variables?

 Not this one! You can define variables with this command:

	create	[type]	[name]	[value]

 This will create a variable of type [type] called [name] with starting value [value]. These will
probably be of type int, used to base clicks on other data, but they can also be booleans and
colors. All variables are for use only within the active script. Scripts run from within that script
will not be able to use those variables. Variables are case-sensitive.
 Here's a bunch of variable creation examples:

	create	int	x	100
	create	boolean	Good?	true
	create	color	white	255	255	255
	create	color	brown	at	123	456
	create	int	y	x
	create	boolean	Good?2	Good?
	create	color	brown2	brown

 Let's go over these examples!

 In the first example, the variable x was created as an integer. To create an integer, the only
argument that must be supplied is the value to store to it.
 In the second example, Good? is created as a boolean. The value stored to it is the value produced
by a variety of clauses which will be covered later in the README.
 In the third example, white was created as a color, a type used for special commands that track
colors. This is one of two ways to initiate a color, both of which take three arguments. The first
way, used to create white, is to give the red, green, and blue values of a color, in that order.
 In the fourth example, brown demonstrates the second way to create a color: the arguments are the
word "at", followed by two more arguments- the x and y coordinates of a pixel location. The value
stored to the variable is the color at that pixel.

 So now that we have variables, we can use them in other commands! Using the above variable
assignments, the command

	left	x	y

would left click at x-100, y-100.
 In the fifth, sixth, and seventh examples, variables were created using other variables as an
argument. This will create a variable with the value of the argument variable. In addition to
copying variables, they can also be used as regular arguments. This command,

	create	color	brown	x	y	z

would create a color with a red value of 100, green value of 100, and blue value between 0 and 29.
 Color variables are used to replace sets of 3 arguments that make up a color. Right now, only
pauses use colors, but they are used later in the README. Assuming the same variable assignments,

	pause	123	456	white

would pause until the pixel is white. The extra argument at the end will still function, so

	pause	123	456	white	1000

will quit out after a second.
 Creating multiple variables with names that are used elsewhere will not cause an error, but may
cause problems when running the script. Be careful when naming variables!

 In addition to creating variables, variables already created can be set to new values like this:

	set	[name]	[value...]

 Setting variables works just like creating variables, for all types of variables, except you don't
need a variable type. Set changes the variable of name [name] to the value that the arguments
[value...] produce. Variables can also technically be changed with create, as the interpreter hits
the most recent entry first, but this is generally not recommended.
 Here is an example of set:

	set	x	200

 Still assuming the same variable assignments, this would change x's value to be 200. So now,

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
line. This means that arguments should be typed like "alphaat,123,456" and "beta255,255,255" for the
following commands:

	create	color	test1	0	0	0
	create	color	test2	0	0	0
	setargto	alpha	test1
	setargto	beta	test2

 These will end up acting as if you had these commands instead:

	create	color	test1	at	123	456
	create	color	test2	255	255	255

 If you call setargto and there was no argument that started with [prefix], nothing will happen.


	Now what good is a variable you can't change?


 Run couldn't tell you, because you CAN change variables, integers at least! There are 5 change
commands in run:

	+=	[name]	[value]
	-=	[name]	[value]
	*=	[name]	[value]
	/=	[name]	[value]
	%=	[name]	[value]

 The first four will respectively add, subtract, multiply, and divide the value of [name] by
[value], and store the result back in [name]. The fifth one will set name to the remainder of [name]
divided by [value]. Here's an example:

	create	y	1
	+=	y	1
	*=	y	9
	%=	y	5
	-=	y	11
	/=	y	-8

 This will create a variable y equal to 1, add 1 to it to give it a value of 2, multiply it by 9 to
give it a value of 18, set it to 3 (the remainder of 18 / 5), subtract 11 to set it to -8, and
divide it by -8 to set it back to 1. All operators work like most programming languages with 32-bit
integers: division will result in an integer rounded down, overflows will make the number go crazy.


	The purpose of these change commands is to give variables values relative to their own value, but
	what if you have a more complex expression that you want to assign to your variable?


 Well fortunately for you, Run supports standard arithmetic! You can assign values to variables in
relation to numbers and any variable, including itself. Let's go over how to use arithmetic:

	set	var	+	[val1]	[val2]
	set	var	-	[val1]	[val2]
	set	var	*	[val1]	[val2]
	set	var	/	[val1]	[val2]
	set	var	%	[val1]	[val2]
	set	var	random	[val1]

 Each of these symboles turns into a number depending on what the values following it are. All
arithmetic can be used in both create and set, as well as in setargto. In the first example,

	set	var	+	[val1]	[val2]

the variable called var would be given a value equal to the addition of [val1] and [val2]; the
following,

	create	int	var	+	2	3


will create a variable called var and set it to 5. If you then had a command,

	set	var	+	var	2

var would be set to 7, which is equivalent to

	+=	var	2

 All of the first five symbols act equivalently to their change-command counterpart, in the same
manner that addition was shown above.
 The sixth example,

	set	var	random	[val1]

var was set to random value. Using "random" will assign the variable to a number ranging from 0 to
the argument that follows, minus 1. So in this example,

	set	var	random	30

var would be assigned a number from 0 to 29 inclusive.
 But arithmetic would be no good if we couldn't use it to create complex expressions! Say you had
the following math expression:

	(A + B - ((A + B) % 2))

 In order to turn it into an expression usable by Run, first separate the operators so that each
operator only acts on two integers, as this is how Run interprets the expression:

	((A + B) - ((A + B) % 2))

 Next, move all operators to the left side of the parentheses:

	(- (+ A B) (% (+ A B) 2))

 Finally, after removing the parentheses,

	- + A B % + A B 2

the expression is usable in Run! So let's see how this works:

	create	int	A	1
	create	int	B	2
	create	int	C	-	+	A	B	%	+	A	B	2

 The first step Run takes is to replace all variables with their values:

	create	int	C	-	+	1	2	%	+	1	2	2

 After that, Run starts in on evaluating. How this works is that the pointer starts from the right,
and goes left along the expression, evaluating the operators as they go. The last four are integers,
so Run goes right by them. As soon as the pointer reaches an operator, it uses it in conjunction
with the variables that follow it. For instance, the pointer in this example starts at the 30 and
goes left until it readches the +, and then stores the value of 1 + 2. like so:

	create	int	C	-	+	1	2	%	+	1	2	2
	create	int	C	-	+	1	2	%	3			2

 The rest of the expression would evaluate in the following manner:

	create	int	C	-	+	1	2	1
	create	int	C	-	3			1
	create	int	C	2

 In the end, C gets set to 2, the result of the expression.

 Well, now that we've got arithmetic covered, Run also includes some useful special system
variables! The variables

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



================================================================
SECTION 3- Programming Language Features- Conditionals and Loops
================================================================

 Yep, conditionals and for loops, that's what!
 Earlier, it was mentioned that you could create variables of type boolean. The main use of these
is for the if commands that look like this:

	if	[boolean]
		[command1]
	[command2]

 The way this works is that if the boolean is false, the next line gets skipped. You can also view
it like this: if and only if the boolean is true, then the line next gets called. So if [boolean]
is true, both [command1] and [command2] get called. If [boolean] is false, [command1] doesn't get
called.
 In addition to this format, if has another syntax that can be used, and it looks like this:

	if	[boolean]
	[
		[command1]
		[command2]
		...
	]
	[some-other-commands]

 Right after the if command, a line with a "[" bracket at the beginning signifies the start of the
commands to be run. If the boolean is false, the pointer jumps to the "]" bracket and continues. You
can also use "(" parentheses and "{" braces, or mix and match them, and they will all work the same.
 These can be paired with jumps for more complex if conditionals:

	create	int	x	100
	create	int	y	100
	if	true
		jump	thisisalabel
	set	x	150
	jump	end
	:thisisalabel
	set	y	150
	jump	end
	:end

 This will set y to 150 since the value given to it is true. If the true were replaced with false,
x would get set to 150. The above is also equivalent to the following:

	create	int	x	100
	create	int	y	100
	if	true
	[
		set	x	150
		jump	end
	]
	set	y	150
	jump	end
	:end

 Stored booleans are not the only booleans that can be put in an if clause. Ifs have multiple forms
with different comparisons. Mentioned earlier was the ability to store clause values to boolean
variables. These are those clauses. Here is one form:

	create	boolean	test	true
	if	test

 The clause is true if the boolean is true. Booleans were already mentioned as able to store to
other booleans. Here is another form:

	if	input	[arg]

 This evaluates to true if [arg] was given as an argument to the program. For instance, calling

	java run thisisascript hello

would make "hello" be an argument. These ifs,

	if	input	goodbye
	if	input	hello
	if	input	he
	if	input	helloo

would evaluate to false, true, false, and false respectively. Remember, you can also use these
clauses to store boolean variables.
 There are four integer-specific evaluation clauses:

	if	>=	[value1]	[value2]
	if	>	[value1]	[value2]
	if	<=	[value1]	[value2]
	if	<	[value1]	[value2]

 These are pretty straight-forward, they evaluate to true if [value1] is greater-than-or-equal-to,
greater than, less-than-or-equal-to, or less than [value2], respectively. There is an equals, but
we'll get to that later.
 There are two clause forms that deal with pixel colors. The first one,

	if	colorat	[x]	[y]	[r]	[g]	[b]

evaluates to true if the pixel at x-[x], y-[y] is r-[r], g-[g], b-[b]. It also has a format

	if	colorat	[x]	[y]	[color]

which uses the color variables mentioned earlier. The second form,

	if	scan	[x]	[y]	[w]	[h]	[r]	[g]	[b]

takes a screenshot of the rectangle with top-left corner at x-[x], y-[y], with width [w] ahd height
[h]. If any of the pixels in that rectangle are r-[r], g-[g], b-[b], then it returns true. A single
scan runs about as fast as a single colorat search. Like colorats, scan also has the form

	if	scan	[x]	[y]	[w]	[h]	[color]

which uses the color variables. Scanning also introduces two new system variables,

	.scanx
	.scany

which are the x- and y-coordinates of the pixel that triggered the scan to be true. If the scan was
false, they are both 0.
 So now that there are some basic clauses, Run also supports standard boolean logic with the
following five commands:

	if	not	[clause]
	if	or	[clause1]	[clause2]
	if	and	[clause1]	[clause2]
	if	==	[clause1]	[clause2]
	if	!=	[clause1]	[clause2]

 Let's go over how these work. The not clause,

	if	not	[clause]

will evaluate to false if [clause] IS true. For example,

	create	boolean	beta	false
	if	not	beta
		set	beta	true

will end up setting the value of beta to true, since beta is NOT true. The or clause,

	if	or	[clause1]	[clause2]

will end up evaluating to true if [clause1] is true, [clause2] is true, or both are. Say you wanted
to run a command if either of two booleans was true:

	create	boolean	a	true
	creaet	boolean	b	true
	if	and	a	b
		move	100	100

 If both a and b are true, the mouse will move to x-100, y-100. Similarly, the and clause,

	if	and	[clause1]	[clause2]

will evaluate to true only if both [clause1] and [clause2] are true. The last two clauses,

	if	==	[var1]	[var2]
	if	!=	[var1]	[var2]

will evaluate to true if [var1] is equal or not equal to [var2], respectively. Both [var1] and
[var2] can be boolean clauses, integers, or colors. So if you had the following,

	create	int	a	123
	create	int	b	123
	if	==	a	b
		move	100	100

then the mouse would move if a and b were equal.
 Now say that you had a more complex boolean expression such as the following:

	((A and B) or ((B != C) != A))

 In order to use this as a clause for an if or boolean definition, the clause is treated similar to
arithmetic. First move all operators to the left side of the parentheses,

	(or (and A B) (!= (!= B C) A))

remove the parentheses and assign variables,

	create	boolean	A	true
	create	boolean	B	true
	create	boolean	C	false
	create	int	X	1
	create	int	Y	0
	if	or	and	A	B	!=	!=	X	Y	A

and it is a usable clause! Then, after replacing all variables with values, evaluation can begin.

	if	or	and	true	true	!=	!=	true	false	true
	if	or	and	true	true	!=	true			true
	if	or	and	true	true	false
	if	or	true			false
	if	true

 At the end, the result is true, so the if does not skip lines.


 Now on to for loops! A for loop has this form:

	for	[name]	[init]	[max]	[incr]

and ends with the command

	end

 The for loop generates a variable named [name] with a value of [init]. When the code reaches the
end command, it increases [name] by [incr]. If the variable is greater than [max], the pointer jumps
to the line after the end command; otherwise, it jumps back to the top. As for the variables defined
by the for loop, they take on the following properties:
- The variables defined by the for loop are available to all commands.
- If there is a global variable outside the for loop with the same name as that variable, the for
    loop one will get picked if it gets referenced.
- Variables only exist while the for loop is in effect: once the for loop ends, the variables are no
    longer available.
- Nested for loops have access to all variables from the previous for loops.
- Variables created with create commands are global, and will not disappear.
- Jump and call commands can access labels outside of the for loop, but you should make sure you
    return to where it was called. Each end command is paired to a specific for command. If you have
    nested for loops and the pointer reaches the wrong end command because of jumping, the loops will
    function incorrectly.
 Here is an example of a for loop:

	for	x	100	200	10
		left	x	100
		pause	1000
	end

 This will left click at x-100, y-100, pause a second, left click at x-110, y-100, pause a second,
and so on. At the end, it will click at x-200, y-100, pause a second, and then continue on.

 Paired with for loops is the ability to exit them early. This is done with 3 breaking commands of
varying power:

	break
	return
	quit

 Break will exit out of the current loop, and do nothing if it is not in a loop. Return will exit
out of the current script but the program will keep running if another script called it. Quit will
stop all autoclicking and end the program.
 You'll notice that there is no while loop, this is because the same effect can be acheived with
either of the following patterns:

	:label1
	if	not	[condition to keep it running]
		jump label2
	[some commands]
	jump	label1
	:label2

	:label1
	if	[condition to keep it running]
	[
		[some commands]
		jump	label1
	]

 This gives loops more versatility when running.

================================================================
SECTION 4- Miscellaneous Features
================================================================

 In addition to clicking, run also allows you to type. There are three commands for key presses:

	type	[text]
	press	[button]
	release	[button]

 Type will type all remaining characters on the line, tabs spaces and all, uppercase and lowercase
letters. The initial tab or space that follows the type is ignored.
 Press will press down one of the following buttons when substituting [button]:

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

 There are also some miscellaneous options that can be set with these commands:

	optionon	[setting]
	optionoff	[setting]

 This will either turn on or turn off, respectively, certain settings that can be helpful while
running scripts. Optionon settings may take extra arguments, but optionoff only needs the name of
the option. There are currently five available settings. The first one,

	optionon	quitafter	[number]

takes an integer argument, [number]. Since pauses and pausenots only continue once the pixel
changes, the script might run indefinitely if the pixel doesn't change properly. This option will
end the script if [number] milliseconds passes without the pixel changing. Note that this is
different than an extra argument at the end of the pause/pausenot, because that argument will
continue running the script. Quitafter is off by default.
 The second option,

	optionon	endingcall	[label]

will be described later in the README.
 The third option,

	optionon	variance	[number]

is for use with checking pixel colors. The variance is how much higher or lower the color value can
be to be accepted as correct. If the variance is 5, the pixel color values can be between 5 higher
and 5 lower than the given value. Checking for a red of 128 would accept anything from 123 to 133 as
the right color value. Variance is 5 by default. Turning off the variance sets it to 0. Setting a
negative variance is a bad idea.
 The fourth option,

	optionoff	mousequit

allows you to turn off the primary way of ending a script, which is by moving the mouse during a
pause. Turning it off means that the script will not end until it reaches the end of the script, or
the entire Java program is ended. Mousequit is on by default.
 The fifth option,

	optionon	scanorder	[direction]	[direction]

sets the order in which the scan clause runs. The first direction is the primary direction in which
the scan searches the picture, the second direction is the secondary direction. The directions for
the command are up, down, left, and right. If you had right for the first argument and up for the
second, then scans would start at the bottom left corner, go right along the row, and once the end
has been reached, it would move up a row, go the left side, and start again. If you have only rights
and lefts as arguments, the first argument will be the primary direction, and down will be assigned
as the secondary direction. Similarly, having only ups and downs will set right as the secondary
direction. The default scanorder is right then up. Turning off scanorder sets it to the default.

 One last feature, you can print to the command line almost just like you can in Java. The command

	print	[text]

will print everything after the print in the same format as type,

	printval	[name]

will print out the value of the variable named [name], and

	println

will start a new line. Println can also accept text as an argument just like print, printing the
text out and starting a new line.
 Printval can also print out any of the system variables, plus a special system variable,

	printval	.timestamp

that will print the time & date, rather than a variable's value. The timestamp format is a 24-hour
clock displayed HH:MM:SS DD/MM/YYYY.
 Here's an example:

	create	int	test	123
	print	The value of "test" is 
	printval	test
	print	 at
	printval	.timestamp
	print	.
	println
	println	Also, here's a tab:	.

 This would print out these two lines:

The value of "test" is 123 at 0:00:00 1/1/1970.
Also, here's a tab:	.

 The time here would be replaced with the real time, rather than the epoch. Notice the spaces at the
end of the first print line, and at the beginning of the second one. Tabs and spaces get treated
like regular characters until the end of the line, just like in type.
 Mentioned earlier was the

	optionon	endingcall	[label]

option. This option will, at the end of the program, jump to the label [label], and run any of the 3
print commands that come after the label. Any other types of commands after the label will be
ignored. The endingcall option will only work on the original script that was run from the command
line. Endingcall is off by default.



 And that's that! You now know how to create autoclicker scripts! The autoclicker will also
automatically do a quick syntax check of all scripts to be run, but will not catch everything. Also,
all comments get removed upon running the script. If you desire blank lines, for ifs or any other
line skipping, you can use blank labels (lines that only contain ":").

 Run is a growing program, there are always plenty of things that can be added or changed. Feel free
to send an e-mail with suggestions, misspellings, confusions, or bug reports.