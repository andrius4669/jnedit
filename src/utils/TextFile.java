/*
 * Copyright (c) 2014, Domas
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package utils;

/**
 *
 * @author Domas
 */
public class TextFile {
    private String name;
    private String text;
    
    public TextFile(String name, String text){
        this.name = name;
        this.text = text;
    }
    public TextFile(String name){
        this(name, "");
    }
    public String getName(){
        return name;
    }
    public String getText(){
        return text;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setText(String text){
        this.text = text;
    }
    public void insert(int at, String text){
        if(at < 0 || at > this.text.length()) return;
        this.text = this.text.substring(0, at)+text+this.text.substring(at);
    }
    public void erase(int start, int end){
        if(start < 0 || end < 0 || start > text.length() || end > text.length()) return;
        this.text = ((start != 0) ? this.text.substring(0, start):"")+text.substring(end);   
    }

}
