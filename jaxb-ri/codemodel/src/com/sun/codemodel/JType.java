/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */

package com.sun.codemodel;


/**
 * A representation of a type in codeModel.
 *
 * A type is always either primitive ({@link JPrimitiveType}) or
 * a reference type ({@link JClass}).
 */
public abstract class JType implements JGenerable, Comparable {

    /**
     * Obtains a reference to the primitive type object from a type name.
     */
    public static JPrimitiveType parse(JCodeModel codeModel, String typeName) {
        if (typeName.equals("void"))
            return codeModel.VOID;
        else if (typeName.equals("boolean"))
            return codeModel.BOOLEAN;
        else if (typeName.equals("byte"))
            return codeModel.BYTE;
        else if (typeName.equals("short"))
            return codeModel.SHORT;
        else if (typeName.equals("char"))
            return codeModel.CHAR;
        else if (typeName.equals("int"))
            return codeModel.INT;
        else if (typeName.equals("float"))
            return codeModel.FLOAT;
        else if (typeName.equals("long"))
            return codeModel.LONG;
        else if (typeName.equals("double"))
            return codeModel.DOUBLE;
        else
            throw new IllegalArgumentException("Not a primitive type: " + typeName);
    }

    /** Gets the owner code model object. */
    public abstract JCodeModel owner();
    
    /**
     * Gets the full name of the type.
     *
     * See http://java.sun.com/docs/books/jls/second_edition/html/names.doc.html#25430 for the details.
     *
     * @return
     *      Strings like "int", "java.lang.String",
     *      "java.io.File[]". Never null.
     */
    public abstract String fullName();

    /**
     * Gets the binary name of the type.
     *
     * See http://java.sun.com/docs/books/jls/third_edition/html/binaryComp.html#44909
     *
     * @return
     *      Name like "Foo$Bar", "int", "java.lang.String", "java.io.File[]". Never null.
     */
    public String binaryName() {
        return fullName();
    }

    /**
     * Gets the name of this type.
     *
     * @return
     *     Names like "int", "void", "BigInteger".
     */
    public abstract String name();
    
    /**
     * Create an array type of this type.
     * 
     * This method is undefined for primitive void type, which
     * doesn't have any corresponding array representation.
     *
     * @return A {@link JClass} representing the array type
     *         whose element type is this type
     */
    public abstract JClass array();

    /** Tell whether or not this is an array type. */
    public boolean isArray() {
        return false;
    }

    /** Tell whether or not this is a built-in primitive type, such as int or void. */
    public boolean isPrimitive() {
        return false;
    }

    /**
     * If this class is a primitive type, return the boxed class. Otherwise return <tt>this</tt>.
     *
     * <p>
     * For example, for "int", this method returns "java.lang.Integer".
     */
    public abstract JClass boxify();

    /**
     * If this class is a wrapper type for a primitive, return the primitive type.
     * Otherwise return <tt>this</tt>.
     *
     * <p>
     * For example, for "java.lang.Integer", this method returns "int".
     */
    public abstract JType unboxify();

    /**
     * Returns the erasure of this type.
     */
    public JType erasure() {
        return this;
    }
    
    /**
     * Returns true if this is a referenced type.
     */
    public final boolean isReference() {
    	return !isPrimitive();
    }

    /**
     * If this is an array, returns the component type of the array.
     * (T of T[])
     */
    public JType elementType() {
        throw new IllegalArgumentException("Not an array type");
    }

    public String toString() {
        return this.getClass().getName()
                + '(' + fullName() + ')';
    }

    /**
     * Compare two JTypes by FQCN, giving sorting precedence to types
     * that belong to packages java and javax over all others.
     *
     * This method is used to sort generated import statments in a
     * conventional way for readability.
     */
    public int compareTo(Object o) {
        final String rhs = ((JType)o).fullName();
        boolean p = fullName().startsWith("java");
        boolean q = rhs.startsWith("java");

        if( p && !q ) {
            return -1;
        } else if( !p && q ) {
            return 1;
        } else {
            return fullName().compareTo(rhs);
        }
    }
}