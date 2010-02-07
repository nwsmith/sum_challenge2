sudo apt-get install python-dev
Download cython from www.cython.org/#download
Untar and follow instructions in INSTALL.txt

Then in the dir with finder.pyx and setup.py do:
python setup.py build_ext --inplace

You should now have a finder.so file (included)

./timer.rb ./main.py ruby duck words.txt
