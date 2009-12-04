/*
 * Copyright 2009 Brendan Kenny
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gwt.ns.transforms.client.impl;

import gwt.ns.transforms.client.Transform;

/**
 * Java implementation of an affine transformation using a 4x4 matrix.<br><br>
 * 
 * <em>Note:</em> This implementation (for now) is column-major, per W3C SVG
 * specifications, so local transforms are done on the right, view transforms
 * are done on the left. This choice has been made explicit in the method
 * signatures: transforms are, by default, local transforms, unless the "view"
 * variant is selected. If internal matrix entries are accessed, however,
 * be aware that the translation vector can be found in the fourth column.
 *
 */
public class TransformImplDefault extends Transform {
	/**
	 * Representation of current transform
	 */
	protected Matrix4x4 transform = new Matrix4x4();
	
	/**
	 * "Scratch" matrix needed for transformations.
	 * TODO: Investigate making static to save memory.
	 */
	private Matrix4x4 temp = new Matrix4x4();
	
	
	/**
	 * Construct a new transform, set to identity
	 */
	public TransformImplDefault() { }
	
	@Override
	public void rotate(double angle) {
		Matrix4x4.rotate(transform, angle);
	}

	@Override
	public void rotateAtPoint(double angle, double px, double py) {
		// TODO: optimize this to reduce matrix operations.
		// possibly refactor to combine with viewRotateAtPoint
		Matrix4x4.translate(transform, px, py);
		Matrix4x4.rotate(transform, angle);
		Matrix4x4.translate(transform, -px, -py);
	}

	@Override
	public void scale(double sx, double sy) {
		Matrix4x4.scale(transform, sx, sy);
	}

	@Override
	public void scaleAtPoint(double sx, double sy, double px, double py) {
		// TODO: optimize this to reduce matrix operations,
		// possibly refactor to combine with viewScaleAtPoint
		Matrix4x4.translate(transform, px, py);
		Matrix4x4.scale(transform, sx, sy);
		Matrix4x4.translate(transform, -px, -py);
	}

	@Override
	public void translate(double tx, double ty) {
		Matrix4x4.translate(transform, tx, ty);
	}


	@Override
	public void skewX(double angle) {
		Matrix4x4.skewX(transform, angle);
	}

	@Override
	public void skewXView(double angle) {
		Matrix4x4.identity(temp);
		Matrix4x4.skewX(temp, angle);
		
		Matrix4x4.multiplyView(transform, temp);
	}
	
	@Override
	public void skewY(double angle) {
		Matrix4x4.skewY(transform, angle);
	}

	@Override
	public void skewYView(double angle) {
		Matrix4x4.identity(temp);
		Matrix4x4.skewY(temp, angle);
		
		Matrix4x4.multiplyView(transform, temp);
	}
	
	@Override
	public void rotateView(double angle) {
		Matrix4x4.identity(temp);
		Matrix4x4.rotate(temp, angle);
		
		Matrix4x4.multiplyView(transform, temp);
	}

	@Override
	public void rotateViewAtPoint(double angle, double px, double py) {
		// TODO: optimize this to reduce matrix operations.
		// possibly refactor to combine with userRotateAtPoint
		// TODO: check order of ops
		Matrix4x4.identity(temp);
		Matrix4x4.translate(temp, px, py);
		Matrix4x4.rotate(temp, angle);
		Matrix4x4.translate(temp, -px, -py);
		
		Matrix4x4.multiplyView(transform, temp);
	}
	
	@Override
	public void scaleView(double sx, double sy) {
		transform.m11 *= sx;
		transform.m12 *= sx;
		transform.m13 *= sx;
		transform.m14 *= sx;
		
		transform.m21 *= sy;
		transform.m22 *= sy;
		transform.m23 *= sy;
		transform.m24 *= sy;
	}

	@Override
	public void scaleViewAtPoint(double sx, double sy, double px, double py) {
		// TODO: optimize this to reduce matrix operations,
		// possibly refactor to combine with userScaleAtPoint
		// TODO: check order of ops
		Matrix4x4.identity(temp);
		Matrix4x4.translate(temp, px, py);
		Matrix4x4.scale(temp, sx, sy);
		Matrix4x4.translate(temp, -px, -py);
		
		Matrix4x4.multiplyView(transform, temp);
	}

	@Override
	public void translateView(double tx, double ty) {
		// skip the extra matrix work
		transform.m14 += tx;
		transform.m24 += ty;
	}

	@Override
	public void setToIdentity() {
		Matrix4x4.identity(transform);
	}

	@Override
	public double m11() {
		return transform.m11;
	}

	@Override
	public double m12() {
		return transform.m12;
	}

	@Override
	public double m13() {
		return transform.m13;
	}

	@Override
	public double m14() {
		return transform.m14;
	}

	@Override
	public double m21() {
		return transform.m21;
	}

	@Override
	public double m22() {
		return transform.m22;
	}

	@Override
	public double m23() {
		return transform.m23;
	}

	@Override
	public double m24() {
		return transform.m24;
	}

	@Override
	public double m31() {
		return transform.m31;
	}

	@Override
	public double m32() {
		return transform.m32;
	}

	@Override
	public double m33() {
		return transform.m33;
	}

	@Override
	public double m34() {
		return transform.m34;
	}

	@Override
	public double m41() {
		return transform.m41;
	}

	@Override
	public double m42() {
		return transform.m42;
	}

	@Override
	public double m43() {
		return transform.m43;
	}

	@Override
	public double m44() {
		return transform.m44;
	}

	@Override
	public void setM11(double m11) {
		transform.m11 = m11;
	}

	@Override
	public void setM12(double m12) {
		transform.m12 = m12;
	}

	@Override
	public void setM13(double m13) {
		transform.m13 = m13;
	}

	@Override
	public void setM14(double m14) {
		transform.m14 = m14;
	}

	@Override
	public void setM21(double m21) {
		transform.m21 = m21;
	}

	@Override
	public void setM22(double m22) {
		transform.m22 = m22;
	}

	@Override
	public void setM23(double m23) {
		transform.m23 = m23;
	}

	@Override
	public void setM24(double m24) {
		transform.m24 = m24;
	}

	@Override
	public void setM31(double m31) {
		transform.m31 = m31;
	}

	@Override
	public void setM32(double m32) {
		transform.m32 = m32;
	}

	@Override
	public void setM33(double m33) {
		transform.m33 = m33;
	}

	@Override
	public void setM34(double m34) {
		transform.m34 = m34;
	}

	@Override
	public void setM41(double m41) {
		transform.m41 = m41;
	}

	@Override
	public void setM42(double m42) {
		transform.m42 = m42;
	}

	@Override
	public void setM43(double m43) {
		transform.m43 = m43;
	}

	@Override
	public void setM44(double m44) {
		transform.m44 = m44;
	}
}
