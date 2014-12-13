/*
 * Copyright (c) 2014, andrius
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
 * @author andrius
 */
public class FileBuffer {
	private String filename;
	public final StringBuffer buf = new StringBuffer();
	
	public FileBuffer() {}
	public FileBuffer(String n) { filename = n; }
	
	public String getName() { return filename; }
	public void rename(String n) { filename = n; }
	
	public void clearText() {
		buf.delete(0, buf.length());
	}
	
	public void remove(int start, int len) {
		if(start < 0 || start >= buf.length()) return;
		if(len > 0) len = start + len;
		else len = buf.length();
		if(len < start) return;
		if(len > buf.length()) len = buf.length();
		buf.delete(start, len);
	}
	
	public void unescapeAndInsert(int start, String text) {
		if(start < 0) start = 0;
		if(start > buf.length()) start = buf.length();
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c != '\\') { buf.insert(start, c); start++; }
			else
			{
				i++;
				if(i >= text.length()) break;
				c = text.charAt(i);
				switch(c) {
					case 'n': buf.insert(start, '\n'); break;
					case 't': buf.insert(start, '\t'); break;
					case 's': buf.insert(start, ' '); break;
					default: buf.insert(start, c); break;	/* for \\ */
				}
				start++;
			}
		}
	}
	
	public void parseEscapedText(String text) {
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c != '\\') buf.append(c);
			else
			{
				i++;
				if(i >= text.length()) break;
				c = text.charAt(i);
				switch(c) {
					case 'n': buf.append('\n'); break;
					case 't': buf.append('\t'); break;
					case 's': buf.append(' '); break;
					default: buf.append(c); break;	/* for \\ */
				}
			}
		}

	}
	
	public void putEscapedText(StringBuilder sb)
	{
		for(int i = 0; i < buf.length(); i++) {
			char c = buf.charAt(i);
			switch(c)
			{
				case '\n': sb.append("\\n"); break;
				case '\t': sb.append("\\t"); break;
				case ' ': sb.append("\\s"); break;
				case '\\': sb.append("\\\\"); break;
				default: sb.append(c); break;
			}
		}
	}
}
