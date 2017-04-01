# portret

A basic image worker on top of graphics-magick with a nice URL/REST endpoint scheme.

## Installation

Download from https://github.com/johanmynhardt/portret.git.

## Usage

Running the server:

    $ java -jar portret-0.1.0-standalone.jar [args]

## ENDPOINTS

### Resize
 `/resize/:dims/s/:source[?sizing=(contain|cover)]`

* `:dims`: dimensions, eg.: `400x200`
* `:source`: encoded source URL, eg.: `http://localhost/example.png` -> `http:%2f%2flocalhost%2fexample.png`

### Crop

 `/crop/:dims/c/:crop-dims/s/:source[?offset=x:y&sizing=(contain|cover)]`

* `:dims`: dimensions
* `:crop-dims`: crop dimensions
* `:source`: encoded source URL
* `offset`: offset from center, eg.: `50:50` or `-100:0`

## Examples

`http://johan-portret.example.com/resize/800x300/s/http%3A%2F%2Fjohan-python.example.com%2Favatar-512.png?sizing=cover`

`http://johan-portret.example.com/crop/800x300/c/300x300/s/http%3A%2F%2Fjohan-python.example.com%2Favatar-512.png?offset=100:100`

## License

Copyright © 2017 Johan Mynhardt

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
