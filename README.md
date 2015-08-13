# Outreach Schedules

Small Clojure library (script, rather) for auto-generating a SQL printout for all sponsor schedules.

## Usage

1. Open `./src/core.clj`
2. Update sponsor ids. Sponsor ids are a list of exclusive sponsors (Dataclip: Exclusive Sponsors)
3. Update p-value.
4. Update holiday list.
5. Evaluate the file in the REPL.
5. Run the `write-sql` command shown in the comments.
6. Delete all schedules in sponsors-production database: `delete from sponsor_schedules;`
7. Upload new schedules:

```
heroku pg:psql --app sponsors-production < /tmp/schedules.sql
```

## License

Copyright © 2015 Banzai Inc.
