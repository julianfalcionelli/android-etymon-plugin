/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps.exceptions;

public class LocationException extends Exception {

    public LocationException(String message) {
        super(message);
    }

    public static class EmptyLocationException extends LocationException {
        public EmptyLocationException() {
            super("Couldn't retrieve current user location");
        }
    }
}
