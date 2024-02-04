%define last_pointer 0
%macro colon 2
    %ifid %2
        %2: dq last_pointer
        %define last_pointer %2
    %else
        %error "not id"
    %endif
    %ifstr %1
        db %1, 0
    %else
        %error "not string"
    %endif
%endmacro
