XMLSpreadsheetGenerator
=======================

XMLSpreadsheetGenerator - Java library that writes XML spreadsheet documents according to Excel 2003 specification

Written in Java, built with Gradle - just because why not :P

Simple Java implementation of the XML spreadsheet document specification by Microsoft.  Similar to SXSSF in its streaming
design; the Generator will flush to the output everything that is written into it, in real time, keeping in memory just a
narrow output buffer for performance.  The library idea is to have a constant memory consumption, independently of the size
of the spreadsheet that we are building - an interesting feature for a web app with a limited and predefined memory heap
