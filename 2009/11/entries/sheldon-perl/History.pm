package History;
sub new {
    my $self = {played => {}};
    return bless($self, $_[0]);
}

sub get_opponent {
    my $self = shift;
    my $wanted = shift;
    return $self->{played}{$wanted};
}

sub store_opponent {
    my $self = shift;
    my $player = shift;
    my $game = shift;
    $self->{played}{$player} = $game;
}
1;
