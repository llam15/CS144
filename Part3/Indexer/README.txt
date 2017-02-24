We created the indexes: item_id, name, and content. Item_id and name are
StringField indexes on the item_id and item name. These are stored because
they will be returned when queried. The content index is a TextField index
that consists of the union of item_id, name, description, and item categories.
This is not stored, because it is only used for queries and is not returned.