package com.w2m.spacecraft.infrastructure.aspect;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NegativeIdAspectTest {


  @Test
  @DisplayName("should trace info when id is negative")
  void shouldTraceInfoWhenIdIsNegative() throws Throwable {

    var logCaptor = LogCaptor.forClass(NegativeIdAspect.class);

    // given
    var identifier = -1L;
    var messageExpected = "Negative identifier found with value: " + String.valueOf(identifier);
    var mockProceedingJoinPoint = new MockProceedingJoinPointImpl(identifier);

    // when
    new NegativeIdAspect().logNegativeId(mockProceedingJoinPoint);

    // then
    assertThat(logCaptor.getInfoLogs()).containsExactly(messageExpected);

  }


  @Test
  @DisplayName("should not trace info when id is not negative")
  void shouldNotTraceInfoWhenIdIsNotNegative() throws Throwable {

    var logCaptor = LogCaptor.forClass(NegativeIdAspect.class);

    // given
    var identifier = 1L;
    var mockProceedingJoinPoint = new MockProceedingJoinPointImpl(identifier);

    // when
    new NegativeIdAspect().logNegativeId(mockProceedingJoinPoint);

    assertThat(logCaptor.getInfoLogs()).isEmpty();

  }


}