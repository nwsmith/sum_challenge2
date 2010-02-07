package Game;
sub new {
    my $self = {plays => []};
    return bless($self, $_[0]);
}

sub play {
    my $self = shift;
    my $play = shift;
    if ($play) {
        push @{ $self->{plays} }, $play;
    } 
    return $self->{plays}->[-1];    
}

sub opponent {
    my $self = shift;
    my $opponent_t = shift;
    if ($opponent_t) {
        $self->{opponent} = $opponent_t;
    }
    return $self->{opponent};
}

sub used_pattern {
    my $self = shift;
    my $pattern = shift;
    if ($pattern) {
        $self->{used_pattern} = $pattern;
    } 
    return $self->{used_pattern};
}

sub play_count {
    my $self = shift;
    my $reset = shift;
    if (defined($reset)) {
        $self->{plays} = [];
    }
    return scalar @{ $self->{plays} };
}

sub finished {
    my $self = shift;
    my $done = shift;
    if ($done) {
        $self->play_count(0);
        $self->{finished} = 1;
    }
    return $self->{finished};
}
1;
