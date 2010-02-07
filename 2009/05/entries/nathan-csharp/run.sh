#!/bin/sh

rm islands.exe >/dev/null 2>&1
gmcs islands.cs
mono islands.exe $*
