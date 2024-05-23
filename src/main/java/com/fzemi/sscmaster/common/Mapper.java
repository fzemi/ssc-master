package com.fzemi.sscmaster.common;

public interface Mapper<A, B> {

    B mapToDTO(A a);

    A mapFromDTO(B b);
}
