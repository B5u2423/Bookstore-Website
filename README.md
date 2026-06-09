# datn2025_1

## Set Up Local Subdomain

On Windows, open `C:\Windows\System32\drivers\etc\host` file, add the following lines to register the domains:

```
127.0.0.1 localhost
127.0.0.1 admin.localhost
```

## References

- https://vuasachcu.com/
- https://www.vinabook.com/
- https://gacxepbookstore.vn/
- https://www.fahasa.com/

## TODO

- [ ] collection view
- [ ] build jar and push to vps

## Local LaTeX Compile

https://www.youtube.com/watch?v=Mty0vHb0knI

## My recommendation

For a bookstore admin dashboard:

Keep one PostgreSQL database.
Separate commands and queries in code.
Write analytics queries using optimized SQL.
Use PostgreSQL views or materialized views for expensive dashboard reports.
Avoid event sourcing, Kafka, separate read databases, or complex CQRS frameworks until you actually hit scaling problems.

This gives you about 80% of the benefits of CQRS with 20% of the complexity.

## Analytics

Sales
Total revenue
Revenue today
Revenue this month
Revenue growth %

Orders
Total orders
Orders today
Average order value

Inventory
Total books
Out-of-stock books
Low-stock books

Books
Top-selling books
Worst-selling books
New arrivals

Customers
Total customers
New customers this month
Repeat purchase rate

Charts
Revenue by month
Orders by day
Top 10 categories
Top 10 books
