#;skadjf
add $s0, $0, $zero
addi $t0, $t0, 100#test comment
slt$t0,$a0,$a1
jr	$ra
or $a0, $a1, $a2
and $a0, $s0, $t1
sub $s5, $s5, $s4
    sll $a0, $a1, 2

lw	$a0, 8($a1)
huh:	sw $a0,4($a1)

jal huh
	bne $sp, $ra, test2	
test2: 
add $ra, $ra, $a1 # comment on label
	beq $a0, $a1, test2

