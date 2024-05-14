package com.w2m.spacecraft.infrastructure.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

import java.util.List;


public class MockProceedingJoinPointImpl implements ProceedingJoinPoint {

    private long identifier;

    public MockProceedingJoinPointImpl (final long identifier) {
        this.identifier = identifier;
    }

    @Override
    public void set$AroundClosure(AroundClosure aroundClosure) {

    }

    @Override
    public Object proceed() throws Throwable {
        return null;
    }

    @Override
    public Object proceed(Object[] objects) throws Throwable {
        return null;
    }

    @Override
    public String toShortString() {
        return "";
    }

    @Override
    public String toLongString() {
        return "";
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object[] getArgs() {
        var list = List.of(this.identifier);
        var array = new Long[list.size()];
        return list.toArray(array);
    }

    @Override
    public Signature getSignature() {
        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return null;
    }

    @Override
    public String getKind() {
        return "";
    }

    @Override
    public StaticPart getStaticPart() {
        return null;
    }
}
