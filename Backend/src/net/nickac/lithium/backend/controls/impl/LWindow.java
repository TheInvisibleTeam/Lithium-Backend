/*
 * MIT License
 *
 * Copyright (c) 2017 NickAc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.nickac.lithium.backend.controls.impl;

import net.nickac.lithium.backend.controls.LContainer;
import net.nickac.lithium.backend.controls.LControl;
import net.nickac.lithium.backend.controls.impl.events.WindowEventHandler;
import net.nickac.lithium.backend.other.LithiumConstants;
import net.nickac.lithium.backend.other.objects.Dimension;
import net.nickac.lithium.backend.other.objects.Point;

import java.util.*;

/**
 * Created by NickAc for Lithium!
 */
public class LWindow extends LControl implements LContainer {

	private transient UUID viewer;
	private HashMap<UUID, LControl> child = new HashMap<>();

	public LWindow() {
		child = new HashMap<>();
	}

	//Event Load
	private transient List<WindowEventHandler> windowLoadHandlers = new ArrayList<>();


	public LWindow onWindowLoad(WindowEventHandler hl) {
		windowLoadHandlers.add(hl);
		return this;
	}

	public void invokeWindowLoad(UUID invoker) {
		windowLoadHandlers.forEach(h -> h.handleEvent(this, invoker));
	}
	//End Event

	//Event Load
	private transient List<WindowEventHandler> windowCloseHandlers = new ArrayList<>();

	public LWindow onWindowUnload(WindowEventHandler hl) {
		windowCloseHandlers.add(hl);
		return this;
	}

	public void invokeWindowClose(UUID invoker) {
		windowCloseHandlers.forEach(h -> h.handleEvent(this, invoker));
	}
	//End Event

	public UUID getViewer() {
		return viewer;
	}

	public void setViewer(UUID viewer) {
		this.viewer = viewer;
	}

	@Override
	public void addControl(LControl c) {
		if (c.getParent() == null) c.setParent(this);
		child.put(c.getUUID(), c);
		refresh();
	}

	@Override
	public void addControl(LControl c, int x, int y, int w, int h) {
		c.setSize(new Dimension(w, h));
		c.setLocation(new Point(x, y));
		addControl(c);
	}

	public void addControl(LControl c, int w, int h) {
		c.setSize(new Dimension(w, h));
		addControl(c);
	}

	public Collection<LControl> getControls() {
		return child.values();
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void removeControl(UUID c) {
		child.remove(c);
		refresh();
	}

	@Override
	public void removeControl(LControl c) {
		removeControl(c.getUUID());
	}

	public void close() {
		if (LithiumConstants.onClose != null && getViewer() != null)
			LithiumConstants.onClose.closeWindow(this, getViewer());
	}
}
