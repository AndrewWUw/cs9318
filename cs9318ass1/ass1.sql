SELECT *
FROM Sale
UNION 

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Location
UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Item
UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Time
UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Location, s.Item
UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Location, s.Time

UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Item, s.Time

UNION

SELECT Location, Item, Time, SUM(Quantity)
FROM Sale s
GROUP BY s.Location, s.Time, s.Item

