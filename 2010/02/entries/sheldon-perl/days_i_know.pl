# we extend Tie::Handle so that we can override print
# this is actually a handy way to redirect old school
# logging without having to modify code (other than 
# including this package)
package DateRanger;
use base Tie::Handle;
use Symbol qw(geniosym);

      
sub TIEHANDLE { return bless geniosym, __PACKAGE__ }

# most of the work happens in here - we use a regex to find & replace the ranges 
# appropriately, and another one to replace digits with their mapped names
sub PRINT {
    shift;
    my $date_map = shift;
    for (@_) {
       s/
         (\d+)            # first number (group #1)
         (?:              # group #2
          ,               #   followed by a comma
          (               #   group #3
           (??{$++1})     #     match previous number + 1 (group 4)
          )               #   end group #3
         )                # end group #4
         {2,}             # group #4 matches at least 2 times
         +                # repeat         
        /$1-$+/gx;        # substitute for the first number followed by last one

        s/(\d)/$date_map->{$1}/g;#replace any digits with their mapped day name
    }
    print $OLD_STDOUT join('', @_);
}

tie *PRINTOUT, 'DateRanger';
our $OLD_STDOUT = select(*PRINTOUT);

1;

#this is the actual program (uses the package from above)
use strict;
use warnings;
my $m = { 1 => 'Mon',
          2 => 'Tue',
          3 => 'Wed',
          4 => 'Thu',
          5 => 'Fri',
          6 => 'Sat',
          7 => 'Sun' };

print $m, @ARGV;

