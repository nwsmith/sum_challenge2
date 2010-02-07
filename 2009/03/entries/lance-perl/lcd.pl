#!/usr/bin/perl -w

use strict;
my(@charMap, $input, $size, $horChar, $verChar, @inputChars, $segmentToCheck, $segsToCheck, $row, $multirow, $i);

# this defines what segments are on for each digit
@charMap = (0x77, 0x12, 0x5D, 0x5B, 0x3A, 0x6B, 0x6F, 0x52, 0x7F, 0x7A, 0x7E, 0x2F, 0x65, 0x1F, 0x6D, 0x6C);

# Basic error checking
if ($#ARGV == 1) {
    if ($ARGV[0] =~ /^[0-9a-fA-F]+$/) { 
	$input = $ARGV[0];
    } else { 
        die "Valid characters to display are 0123456789abcdef";
    }

    if ($ARGV[1] =~ /^\d+$/) {
	$size = $ARGV[1];
    } else { 
        die "Second argument needs to be an integer, size of LCD digits\n";
    }
} else {
    die "usage:  <characters to print> <size of LCD digits>";
}


for($i=0; $i < $size; $i++) { $horChar .= "-"; }
$verChar = "|";
@inputChars = split("",$input);
$segmentToCheck = 0;

for($row = 0; $row < 5; $row++) {
    for($multirow = 0; $multirow < $size; $multirow++) {
	foreach(@inputChars) {
	    if($row % 2) {
		$segsToCheck = 2; 
		printVerticalCharacter($segmentToCheck, $segsToCheck, $size, $multirow, $verChar);
	    } else {
		$segsToCheck = 1;
		printHorizontalCharacter($segmentToCheck, $segsToCheck, $size, $multirow, $horChar);
	    }
	}
	print("\n") unless $multirow !=0 && $segsToCheck == 1;
    }
    $segmentToCheck += $segsToCheck;
}

sub printHorizontalCharacter {
    my($segmentToCheck, $segsToCheck, $size, $multirow, $charToPrint, $curSeg, $output);
    $segmentToCheck = shift;
    $segsToCheck = shift;
    $size = shift;
    $multirow = shift;
    $charToPrint = shift;
    return unless $multirow == 0;
    for($curSeg = $segmentToCheck;  $curSeg < ($segmentToCheck+$segsToCheck); $curSeg++) {
	$output = " ".$charToPrint."  ";
	if(!shouldPrintCharacter($_, $curSeg)) {
	    $output =~ s/./ /g;
	} 
	print $output;
    }
}

sub printVerticalCharacter {
    my($segmentToCheck, $segsToCheck, $size, $multirow, $charToPrint, $curSeg, $output);
    $segmentToCheck = shift;
    $segsToCheck = shift;
    $size = shift;
    $multirow = shift;
    $charToPrint = shift;

    for($curSeg = $segmentToCheck;  $curSeg < ($segmentToCheck+$segsToCheck); $curSeg++) {
	$output = $charToPrint;
	if($curSeg == $segmentToCheck) {
	    $output = sprintf("%-${size}s", $output);
	}
	$output .= " ";
	if(shouldPrintCharacter($_, $curSeg)) {
	    print $output;
	} else {
	    $output =~ s/./ /g;
	    print $output;
	}
    }
}

sub shouldPrintCharacter {
    my($charToPrint, $segment,$character, $mask);
    $charToPrint = shift;
    $charToPrint = hex($charToPrint);
    $segment = shift;
    $character = $charMap[$charToPrint];
    $mask = 2 ** (6-$segment);
    if($character & $mask) {
	return 1;
    } else {
	return 0;
    }
}
