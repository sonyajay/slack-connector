fun hoursAgo(hours: Number) : Number = (now() - "PT$(hours)H") as Number

fun daysAgo(days: Number) : Number = (now() - "P$(days)D") as Number

fun yesterday() : Number = (now() - |P1D|) as Number

fun hoursFromNow(hours: Number) : Number = (now() + "PT$(hours)H") as Number

fun minutesFromNow(min: Number) : Number = (now() + "PT$(min)M") as Number