set linesize 9999;
set wrap off;
set pagesize 10000;
set verify off;

with params as
(
select 
'&input' p,
&size sze
from dual
),
szedrows as
(
select '|' a
from params
connect by level <= params.sze 
),
out as
(
select 
  1 n,
  replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
  params.p, 
  '0', ' ' || lpad('-', params.sze, '-') || '  '),
  '1', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '2', ' ' || lpad('-', params.sze, '-') || '  '), 
  '3', ' ' || lpad('-', params.sze, '-') || '  '),
  '4', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '5', ' ' || lpad('-', params.sze, '-') || '  '),
  '6', ' ' || lpad('-', params.sze, '-') || '  '),
  '7', ' ' || lpad('-', params.sze, '-') || '  '),
  '8', ' ' || lpad('-', params.sze, '-') || '  '),
  '9', ' ' || lpad('-', params.sze, '-') || '  ') txt 
from params
union all
select 
  2 n,
  replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
  params.p, 
  '0', '|' || lpad(' ', params.sze, ' ') || '| '),
  '1', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '2', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '3', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '4', '|' || lpad(' ', params.sze, ' ') || '| '),
  '5', '|' || lpad(' ', params.sze, ' ') || '  '),
  '6', '|' || lpad(' ', params.sze, ' ') || '  '),
  '7', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '8', '|' || lpad(' ', params.sze, ' ') || '| '),
  '9', '|' || lpad(' ', params.sze, ' ') || '| ') txt
from szedrows, params
union all
select 
  3 n,
  replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
  params.p, 
  '0', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '1', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '2', ' ' || lpad('-', params.sze, '-') || '  '), 
  '3', ' ' || lpad('-', params.sze, '-') || '  '),
  '4', ' ' || lpad('-', params.sze, '-') || '  '),
  '5', ' ' || lpad('-', params.sze, '-') || '  '),
  '6', ' ' || lpad('-', params.sze, '-') || '  '),
  '7', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '8', ' ' || lpad('-', params.sze, '-') || '  '),
  '9', ' ' || lpad('-', params.sze, '-') || '  ') txt
from params
union all
select 
  4 n,
  replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
  params.p, 
  '0', '|' || lpad(' ', params.sze, ' ') || '| '),
  '1', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '2', '|' || lpad(' ', params.sze, ' ') || '  '),
  '3', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '4', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '5', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '6', '|' || lpad(' ', params.sze, ' ') || '| '),
  '7', ' ' || lpad(' ', params.sze, ' ') || '| '),
  '8', '|' || lpad(' ', params.sze, ' ') || '| '),
  '9', ' ' || lpad(' ', params.sze, ' ') || '| ') txt
from szedrows, params
union all
select 
  5 n,
  replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
  params.p, 
  '0', ' ' || lpad('-', params.sze, '-') || '  '),
  '1', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '2', ' ' || lpad('-', params.sze, '-') || '  '), 
  '3', ' ' || lpad('-', params.sze, '-') || '  '),
  '4', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '5', ' ' || lpad('-', params.sze, '-') || '  '),
  '6', ' ' || lpad('-', params.sze, '-') || '  '),
  '7', ' ' || lpad(' ', params.sze, ' ') || '  '),
  '8', ' ' || lpad('-', params.sze, '-') || '  '),
  '9', ' ' || lpad('-', params.sze, '-') || '  ') txt
from params
)
select out.txt from out order by n;

