package com.fballtech.scoutbasebeta;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Position {
	
	QB,
	RB,
	WR,
	TE,
	OG,
	OT,
	OC,
	IDL,
	DE,
	EDGE,
	OLB,
	ILB,
	CB,
	SAF,
	K,
	P,
	LS
	
}
