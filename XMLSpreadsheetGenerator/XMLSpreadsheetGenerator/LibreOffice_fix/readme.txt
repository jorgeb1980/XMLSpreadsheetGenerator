For the curious of mind and not willing to use dubious origin cracks of commercial 
spreadsheet products, you can try Open Office and Libre Office.  There you will
see that Microsoft XML Spreadsheet format is supported.

My choice to try this was Libre Office 4.1.4.2

However, this format has been finding problems in both forks of the ancient 
Star Office software.  This XSL I am putting here is a humble try to fix a 
little problem with the row-repeating recursive XSL macro wich prevented Libre
Office to save XML Spreadsheets with gaps (empty rows, etc.).

The fix is not intended for production (I guess it can be quite heavy with a
spreadsheet having thousands of empty lines) but it succeeded in saving a 
couple of files, which is all I needed for my purposes.  I found it under:

LOCAL_DIRECTORY/share/xslt/export/spreadsheetml/table.xsl

Do a backup of your original file before tampering with it!!
