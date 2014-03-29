For the curious of mind and not willing to use dubious origin cracks of commercial 
spreadsheet products, you can try Open Office and Libre Office.  There you will
see that Microsoft XML Spreadsheet format is supported.

My choice to try this was Libre Office 4.1.4.2

However, this format has been finding problems in both forks of the ancient 
Star Office software.  Here are included some attempts to fix:
  
+ a problem with the row-repeating recursive XSL macro wich prevented Libre
Office to save XML Spreadsheets with gaps (empty rows, etc.).
+ a problem which prevented Libre Office to save the XML spreadsheet 
with the desired font styles.

The fix is not intended for production but it succeeded in saving a 
couple of files, which is all I needed for my purposes.  I found the
stylesheets under:

LOCAL_DIRECTORY/share/xslt/export/spreadsheetml/*.xsl

Do a backup of your original files before tampering with them!!
