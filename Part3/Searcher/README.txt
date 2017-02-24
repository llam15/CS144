Note on XML special character escaping: 

In the original XML files, there are some instances of special characters such
as quotations in the form of \" that are not escaped. However, when this is
parsed by Java, they are turned into ordinary quotation marks. Therefore, they
are indistinguishable from quotes that should be escaped. As a result, we
simply escape all special characters that appear.