#/bin/bash

# Script meant to run from the wiki/pdf folder ONLY!
# IMPORTANT: Read the HOWTO on how to set things up for this script to run
# Converts UserGuide.wiki to UserGuide.pdf
# Written by: Purnank Ghumalia & Julie Ann Sakuda

# Remove/fix some wiki text that is not supported by wt2db
sed '
/#summary/d
/#labels/d
s/#\./#/g
s/\(\[\S*\)\s/\1\|/
s/_\(\S*\)_/\1/g
s/</\&lt;/g
s/>/\&gt;/g
s/{{{//g
s/}}}//g
s/\[/\[\[/g
s/\]/\]\]/g' < ../UserGuide.wiki > UserGuide.wiki.tmp

# wt2db creates the DocBook XML
wt2db -V UserGuide.wiki.tmp -o UserGuide.xml.tmp

# wt2db creates some malformed XML
# Concat our XML header and footer onto the generated XML to make the XML valid
cat header.xml >> UserGuide.xml.raw
cat UserGuide.xml.tmp >> UserGuide.xml.raw
cat footer.xml >> UserGuide.xml.raw

# Changes the image links to be relative to where the wiki folder has been checked out from SVN
sed -e "s/\(\[\?http:\/\/jupiter-eclipse-plugin.googlecode.com\/svn\/wiki\/\)images\/\(.*\)\.jpg\]\?/"\
"\<mediaobject\>\<imageobject\>\<imagedata fileref=\"\.\.\/images\/"\
"\2\."\
"jpg\" format=\"JPEG\" \/\>\<\/imageobject\>\<\/mediaobject\>/g" UserGuide.xml.raw > UserGuide.xml

# Fix the permissions on DocMan (doesn't run if this isn't right)
chmod -R 755 docman

# Goto the DocMan folder (DocMan ONLY runs if in the DocMan folder!!)
cd docman
./docman --nogui ../ug2pdf.docman

# Go back to the wiki/pdf folder and do some clean up
cd ..
rm UserGuide.wiki.tmp
rm UserGuide.xml.tmp
rm UserGuide.xml.raw
rm UserGuide.xml
