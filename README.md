XMLSpreadsheetGenerator [![Build Status](https://travis-ci.org/jorgeb1980/XMLSpreadsheetGenerator.svg?branch=master)](https://travis-ci.org/jorgeb1980/XMLSpreadsheetGenerator/builds/latest) [![codecov](https://codecov.io/gh/jorgeb1980/XMLSpreadsheetGenerator/branch/master/graph/badge.svg)](https://codecov.io/gh/jorgeb1980/XMLSpreadsheetGenerator)
=======================

XMLSpreadsheetGenerator - Java library that writes XML spreadsheet documents according to Excel 2003 specification

Simple Java implementation of the XML spreadsheet document specification by Microsoft.  Similar to SXSSF in its streaming
design; the Generator will flush to the output everything that is written into it, in real time, keeping in memory just a
narrow output buffer for performance.  The library idea is to have a constant memory consumption, independently of the size
of the spreadsheet that we are building - an interesting feature for a web app with a limited and predefined memory heap

Latest binary at
https://github.com/jorgeb1980/XMLSpreadsheetGenerator/releases/latest
