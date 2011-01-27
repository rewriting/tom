/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package boulderdash;

public class BoulderDashImagesData {
  public static final int NUM_IMAGES = 15;
  public static final int IMAGE_SIZE = 20;
  public static int[][] imgCoord =
  {{  0, 0,20,20},   //0: Space
   { 20, 0,20,20},   //1: Dirt
   { 40, 0,20,20},   //2: Titanium Wall
   {  0,20,20,20},   //3: Brick Wall
   { 20,20,20,20},   //4: Magic Wall
   { 40,20,20,20},   //5: Player
   {  0,40,20,20},   //6: Rock
   { 20,40,20,20},   //7: Diamond
   { 40,40,20,20},   //8: Explosion
   {  0,60,20,20},   //9: Butterfly
   { 20,60,20,20},   //10: Firefly
   { 40,60,20,20},   //11; Amoeba
   {  0,80,20,20},   //12: Closed Exit
   { 20,80,20,20},   //13: Open Exit
   { 40,80,20,20}};  //14: Robot (not programmed)
  
} // Class BoulderDashImagesManager
