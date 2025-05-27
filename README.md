<!-- modrinth_exclude.start -->
# Advanced Tag Loader
<!-- modrinth_exclude.end -->

Vanilla Minecraft allows datapacks to add entries to a specific registry tag,
thereby merging them with the values of datapacks below, or completely replace them with new ones.

This mod allows for a third mode of operation, removing specific entries.
It adds a new optional property to tag files called `remove`, which may contain a list of tag entries like the ones in `values`.
These entries will be excluded from the tag

The example below creates a tag containing all logs, except the ones that can be burned. 
The two properties do not have to be in the same file for this to work,
but can also be part of two separate datapacks layered on top of each other.

`data/example/tags/items/test.json`
````json
{
  "values": [
    "#minecraft:logs"
  ],
  "remove": [
    "#minecraft:logs_that_burn"
  ]
}
````