from py4j.java_gateway import java_import
from utils import datetime_to_millis
import numpy as np
import pandas as pd

class UniformDateTimeIndex:
    """
    A DateTimeIndex maintains a bi-directional mapping between integers and an ordered collection of
    date-times. Multiple date-times may correspond to the same integer, implying multiple samples
    at the same date-time.

    To avoid confusion between the meaning of "index" as it appears in "DateTimeIndex" and "index"
    as a location in an array, in the context of this class, we use "location", or "loc", to refer
    to the latter.
    """ 

    def __init__(self, start, periods, freq, sc, jdt_index = None):
        if jdt_index != None:
          self._jdt_index = jdt_index
        else:
          self._jdt_index = sc._jvm.com.cloudera.sparkts.UniformDateTimeIndex(start, periods, freq)

    def size(self):
        return self._jdt_index.size()

    def first(self):
        millis = self._jdt_index.first().getMillis()
        return pd.Timestamp(millis * 1000000)

    def last(self):
        millis = self._jdt_index.last().getMillis()
        return pd.Timestamp(millis * 1000000)

    def __getitem__(self, val):
        # TODO: throw an error if the step size is defined
        if isinstance(val, slice):
            start = datetime_to_millis(val.start)
            stop = datetime_to_millis(val.stop)
            jdt_index = self._jdt_index.slice(start, stop)
            return UniformDateTimeIndex(None, None, None, None, jdt_index)
        else:
            return self._jdt_index.locAtDateTime(datetime_to_millis(val))

    def islice(self, start, end):
        jdt_index = self._jdt_index.islice(start, end)
        return UniformDateTimeIndex(None, None, None, None, jdt_index)

    def datetime_at_loc(self, loc):
        millis = self._jdt_index.dateTimeAtLoc(loc).getMillis()
        return pd.Timestamp(millis * 1000000)

    def __eq__(self, other):
        return self._jdt_index.equals(other._jdt_index)

    def __ne__(self, other):
        return not self.__eq__(other)

    def __repr__(self):
        return self._jdt_index.toString()

class DayFrequency:
  
    def __init__(self, jfreq):
        self._jfreq = jfreq

    def __init__(self, days, sc):
        self._jfreq = sc._jvm.com.cloudera.sparkts.DayFrequency(days)

    def days(self):
        return self._jfreq.days()

    def __eq__(self, other):
        return self._jfreq.equals(other._jfreq)

    def __ne__(self, other):
       return not self.__eq__(other)

def uniform(start, periods, frequency, sc):
    start = datetime_to_millis(start)
    return UniformDateTimeIndex(start, periods, frequency._jfreq, sc)
