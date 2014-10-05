 For those of you don't want to go through the entire tutorial just for a quick lookup, here is a
list of all the commands and their formats. Parameters are in brackets, parameters with a ... at the
end signify a non-constant number of values. The clauses do not include any ... since the clause
number changes due to evaluation. An asterisk * next to a parameter inside the brackets indicates
that the parameter can be a clause.

####Commands:
```
	left		[x*]	[y*]
	left		[num*]
	right		[x*]	[y*]
	right		[num*]
	middle		[x*]	[y*]
	middle		[num*]
	move		[x*]	[y*]
	pause
	pause		[time*]
	pause		[x*]	[y*]	[r*]	[g*]	[b*]
	pause		[x*]	[y*]	[r*]	[g*]	[b*]	[timeout*]
	pause		[x*]	[y*]	[color]
	pause		[x*]	[y*]	[color]	[timeout*]
	scroll		[direction]
	scroll		[direction]	[amount*]
	pausenot	[x*]	[y*]	[r*]	[g*]	[b*]
	pausenot	[x*]	[y*]	[r*]	[g*]	[b*]	[timeout*]
	pausenot	[x*]	[y*]	[color]
	pausenot	[x*]	[y*]	[color]	[timeout*]
	jump		[label]
	call		[label]
	jumpback
	run		[script]	[input...]
	create		int	[name]	[value*]
	create		boolean	[name]	[value*]
	create		timer	[name]	[value*]
	create		color	[name]	[r*]	[g*]	[b*]
	create		color	[name]	at	[x*]	[y*]
	create		color	[name]	[color]
	create		screenshot	[name]	[x*]	[y*]	[w*]	[h*]
	create		screenshot	[name]	[screenshot]
	set		[int]	[value*]
	set		[boolean]	[value*]
	set		[timer]	[value*]
	set		[color]	[r*]	[g*]	[b*]
	set		[color]	at	[x*]	[y*]
	set		[color]	[othercolor]
	set		[screenshot]	[x*]	[y*]	[w*]	[h*]
	set		[screenshot]	[otherscreenshot]
	setargto	[prefix]	[name]
	+=		[name]	[value*]
	-=		[name]	[value*]
	*=		[name]	[value*]
	/=		[name]	[value*]
	%=		[name]	[value*]
	if		[value*]
	for		[name]	[init]	[max]	[increment]
	end
	break
	return
	quit
	type		.[text]
	press		[button]
	release		[button]
	saveimage	[screenshot]
	loadimage	[name]	[x*]	[y*]
	loadimage	[name]
	optionon	quitafter	[number*]
	optionon	endingcall	[label]
	optionon	variance	[r*]	[g*]	[b*]
	optionon	variance	[value*]
	optionon	mousetolerance	[xtolerance*]	[ytolerance*]
	optionon	mousetolerance	[tolerance*]
	optionon	scanorder	[direction1]	[direction2]
	optionon	offset	[x*]	[y*]
	optionon	endmessages
	optionoff	[setting]
	print		.[text]
	printval	[name]
	println
	println		.[text]
```

####Operators:
```
	input		[arg]
	>=		[val1*]	[val2*]
	>		[val1*]	[val2*]
	<=		[val1*]	[val2*]
	<		[val1*]	[val2*]
	==		[clause1*]	[clause2*]
	!=		[clause1*]	[clause2*]
	not		[clause*]
	or		[clause1*]	[clause2*]
	and		[clause1*]	[clause2*]
	+		[val1*]	[val2*]
	-		[val1*]	[val2*]
	*		[val1*]	[val2*]
	/		[val1*]	[val2*]
	%		[val1*]	[val2*]
	random		[val*]
	colorat		[x*]	[y*]	[r*]	[g*]	[b*]
	colorat		[x*]	[y*]	[color]
	scan		[x*]	[y*]	[w*]	[h*]	[r*]	[g*]	[b*]
	scan		[x*]	[y*]	[w*]	[h*]	[color]
	scan		[screenshot]	[r*]	[g*]	[b*]
	scan		[screenshot]	[color]
	imagecolorat	[screenshot]	[x*]	[y*]	[r*]	[g*]	[b*]
	imagecolorat	[screenshot]	[x*]	[y*]	[color]
	imagecolor	[screenshot]	[x*]	[y*]
	redpart		[color]
	greenpart	[color]
	bluepart	[color]
	imagered	[screenshot]	[x*]	[y*]
	imagegreen	[screenshot]	[x*]	[y*]
	imageblue	[screenshot]	[x*]	[y*]
```

####Other:
```
	:[label]

	if	[clause*]
	[
		[commands-if-true]
		...
	]

	if	[clause*]
		[command-if-true]
	[commands-whether-true-or-not]
	...
```
