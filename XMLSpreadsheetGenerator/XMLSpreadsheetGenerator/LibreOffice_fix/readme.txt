My choice to test the generator was Libre Office 4.1.4.2

However, the XML spreadsheet format has been finding problems in this fork of the ancient 
Star Office software.  Here are included some attempts to fix:
  
+ a problem with the row-repeating recursive XSL macro wich prevented Libre
Office to save XML Spreadsheets with gaps (empty rows, etc.).
+ a problem which prevented Libre Office to save the XML spreadsheet 
with the desired font styles.

The fix is not intended for production but it succeeded in saving a 
couple of files, which is all I needed for my purposes.  I found the
stylesheets under:

LOCAL_DIRECTORY/share/xslt/export/spreadsheetml/*.xsl

Do a backup of your original files if you replace them with the fix!!
I have tried the fix with Libre Office 4.1.4.2 and Open Office 4.0.1 
and it seems to work nice, at least as far as I need it.
